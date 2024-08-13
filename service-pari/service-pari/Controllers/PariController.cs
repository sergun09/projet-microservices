using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using service_pari.Model.Dto;
using service_pari.Model.Entity;
using service_pari.Model.Exceptions;
using service_pari.Model.Service;
using service_pari.Utilities.RabbitMQ;

namespace service_pari.Controllers
{
    [Route("paris")]
    [ApiController]
    //[ApiVersion("1.0")]
    public class PariController : ControllerBase
    {
        private readonly IPariService pariService;

        private readonly IRabbitMQSenderService _senderService;

        private readonly IPariOuvertService _pariOuvertService;


        public PariController(IPariService pariService, IRabbitMQSenderService senderService, IPariOuvertService pariOuvertService)
        {
            this.pariService = pariService;
            _senderService = senderService;
            _pariOuvertService = pariOuvertService;
        }

        [HttpGet]
        [Authorize(Roles = "ROLE_ADMIN")]
        public async Task<ActionResult<PagedResult<Pari>>> GetParis([FromQuery] int pageIndex = 1, [FromQuery ]int pageSize = 10) 
        {
            PagedResult<Pari> paris = await pariService.GetParisAsync(pageIndex, pageSize);
            paris.currentPage = pageIndex;
            paris.totalPages = (paris.totalItems + pageSize - 1) / pageSize;
            return base.Ok(paris);
        }

        [HttpGet("evenements")]
        [Authorize(Roles = "ROLE_USER,ROLE_ADMIN")]
        public ActionResult<List<EventCountResult>> GetNbParisByEvent()
        {
            return Ok(pariService.GetNbParisByEvent());
        }


        [HttpGet("utilisateurs")]
        [Authorize(Roles = "ROLE_USER,ROLE_ADMIN")]
        public async Task<ActionResult<PagedResult<Pari>>> GetParisByUser([FromQuery] int pageIndex = 1, [FromQuery] int pageSize = 10) 
        {
            var userId = int.Parse(User.Claims.Where(c => c.Type == "idUtilisateur").FirstOrDefault().Value);
            var paris = await pariService.GetParisByUserAsync(userId,pageIndex, pageSize);
            paris.currentPage = pageIndex;
            paris.totalPages = (paris.totalItems + pageSize - 1) / pageSize;
            // On essaie de consulter les paris qui ne nous appartiennent pas
            if (paris.items.Count > 0) 
            {
                if (userId != paris.items.First().UtilisateurId)
                    return Forbid();

            }
            return Ok(paris);
        }
        
        [HttpGet("{id}")]
        [Authorize(Roles = "ROLE_USER,ROLE_ADMIN")]
        public async Task<ActionResult<Pari>> GetPari(int id)
        {
            try 
            {
                Pari pari = await pariService.GetPariByIdAsync(id);

                // On essaie de récupérer un pari qui ne nous appartient pas
                if (int.Parse(User.Claims.Where(c => c.Type == "idUtilisateur").FirstOrDefault().Value) != pari.UtilisateurId)
                    return Forbid();

                return pari;
            }
            catch (PariInexistantException ex)
            {
                return NotFound(new ApiErrorResponse(ex.Message));
            }
        }

        [HttpDelete("{id}")]
        [Authorize(Roles = "ROLE_USER,ROLE_ADMIN")]
        public async Task<ActionResult<Pari>> DeletePari(int id)
        {
            try
            {
                Pari pari = await pariService.GetPariByIdAsync(id);

                // On essaie de supprimer un pari qui ne nous appartient pas
                if (int.Parse(User.Claims.Where(c => c.Type == "idUtilisateur").FirstOrDefault().Value) != pari.UtilisateurId)
                    return Forbid();


                PariOuvert pariOuvert = _pariOuvertService.GetPariOuvert(pari.EvenementId);

                if (pariOuvert is null)
                {
                    pariService.DeletePari(pari);
                    return Ok();
                } 

                if (DateTime.UtcNow > pariOuvert.DateLimite) 
                {
                    pariService.DeletePari(pari);
                    Console.WriteLine("Pari supprimé et non remboursé !");
                    return Ok();
                }

                pariService.DeletePari(pari);
                _senderService.SendMessage(new PaiementAnnuleDTO
                {
                    montant = pari.Mise,
                    idPari = id,
                    idUtilisateur = int.Parse(User.Claims.Where(c => c.Type == "idUtilisateur").FirstOrDefault().Value),
                }, "pari.annule", "pari.annule");
                return Ok();
            }
            catch (PariInexistantException e)
            {
                return NotFound(new ApiErrorResponse(e.Message));
            }
        }

        [HttpPost]
        [Authorize(Roles = "ROLE_USER,ROLE_ADMIN")]
        public async Task<ActionResult<Pari>> AddPari([FromBody] PariDTO pariDto)
        {
            if (pariDto.Mise <= 0)
            {
                return BadRequest(new ApiErrorResponse("Le montant misé doit être strictement supérieur à 0 !"));
            }

            if (pariDto.Prediction is null)
            {
                return BadRequest(new ApiErrorResponse("La prediction du pari est incorrect"));
            }

            PariOuvert pariOuvert = _pariOuvertService.GetPariOuvert(pariDto.EvenementId);

            if (pariOuvert is null)
                return NotFound(new ApiErrorResponse("Vous ne pouvez pas parier sur pariOuvert inexistant"));

            Console.WriteLine($"Date Ajd : {DateTime.Now} ||||| Date Limite : {pariOuvert.DateLimite}");

            if (DateTime.Compare(DateTime.UtcNow, pariOuvert.DateLimite) > 0)
                return BadRequest(new ApiErrorResponse("Vous ne pouvez pas parier sur un évènement qui a déjà commencé !"));

            if (pariOuvert is not null ) 
            {
                if (ModelState.IsValid)
                {
                    var userId = User.Claims.Where(c => c.Type == "idUtilisateur").FirstOrDefault().Value;
                    Pari pari = await pariService.AddPariAsync(pariDto, int.Parse(userId));

                    return Created(new Uri($"http://localhost:8080/api/paris/{pari.Id}"), pari);
                }
            }
            return BadRequest(new ApiErrorResponse("Impossible de parier")); ;
        }
    }
}
