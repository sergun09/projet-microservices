using service_pari.Model.Dao.Repository;
using service_pari.Model.Dto;
using service_pari.Model.Entity;
using service_pari.Model.Exceptions;
using service_pari.Utilities.RabbitMQ;

namespace service_pari.Model.Service;

public class PariService : IPariService
{
    private readonly IPariRepository _pariRepository;

    private readonly IRabbitMQSenderService _senderService;

    private readonly IPariOuvertService _pariOuvertService;

    public PariService(IPariRepository pariRepository, IRabbitMQSenderService senderService, IPariOuvertService pariOuvertService)
    {
        _pariRepository = pariRepository;
        _senderService = senderService;
        _pariOuvertService = pariOuvertService;
    }

    public async Task<Pari> GetPariByIdAsync(int pariId)
    {
        Pari pari = await _pariRepository.GetPariByIdAsync(pariId);
        if (pari is null)
            throw new PariInexistantException($"Le pari avec l'id {pariId} n'existe pas");
        return pari;
    }

    public async Task<PagedResult<Pari>> GetParisAsync(int pageIndex, int pageSize)
    {
        return await _pariRepository.GetParisAsync(pageIndex, pageSize);
    }

    public Task<PagedResult<Pari>> GetParisByUserAsync(int utilisateurId, int pageIndex, int pageSize)
    {
        return _pariRepository.GetParisByUserAsync(utilisateurId, pageIndex, pageSize);
    }

    public async Task<Pari> AddPariAsync(PariDTO pariDto, int utilisateurId)
    {
        Pari pari = new Pari()
        {
            EvenementId = pariDto.EvenementId,
            UtilisateurId = utilisateurId,
            TransactionId = pariDto.TransactionId,
            Mise = pariDto.Mise,
            Prediction = Enum.Parse<Prediction>(pariDto.Prediction)
        };
        await _pariRepository.AddPariAsync(pari);
        return pari;
    }

    public void DeletePari(Pari pari)
    {
        _pariRepository.DeletePari(pari);
    }

    public Task verificationPari(EvenementResultatDTO resultatDTO)
    {
        IReadOnlyList<Pari> paris = GetParisByEvenementId(resultatDTO.idEvenement);
        Console.WriteLine($"Evenement reçu avec la cote : ${resultatDTO.coteResultat}");
        foreach(var pari in paris) 
        {
            Console.WriteLine($"Prédiction de votre pari : {pari.Prediction.ToString()} || " +
                    $"Résultat de k'évènement : {resultatDTO.typeResultat}");
            if (pari.Prediction.ToString() == resultatDTO.typeResultat) 
            {

                Console.WriteLine($"Prediction BOOOOOOOOOOOOOOOOOOOOOOOOOONNEEE avec l'id : {pari.Id}");
                double gainPari = pari.Mise * resultatDTO.coteResultat;
                _senderService.SendMessage(new 
                {
                    gain = gainPari,
                    idPari = pari.Id,
                    idUtilisateur = pari.UtilisateurId
                }, "pari.gagne", "pari.gagne.paiement.confimation");
                Console.WriteLine($"------------- Pari Gagné et envoyé avec l'id {pari.Id}" );
            }
            else 
            {
                Console.WriteLine("Pari perdu :(");
            }
        }
        _pariOuvertService.DeletePariOuvert(resultatDTO.idEvenement);
        return Task.CompletedTask;
    }

    public IReadOnlyList<Pari> GetParisByEvenementId(int idEvenement)
    {
        return _pariRepository.GetParisByEvenementId(idEvenement);
    }

    public List<EventCountResult> GetNbParisByEvent()
    {
        return _pariRepository.GetNbParisByEvent();
    }

    public void DeleteParis(int idEvenement)
    {
        IReadOnlyList<Pari> paris = _pariRepository.GetParisByEvenementId(idEvenement);
        PariOuvert pariOuvert = _pariOuvertService.GetPariOuvert(idEvenement);
        foreach(var pari in paris) 
        {
            if(DateTime.UtcNow < pariOuvert.DateLimite) 
            {
                _pariRepository.DeletePari(pari);
                _pariOuvertService.DeletePariOuvertByIdEvenement(idEvenement);
                _senderService.SendMessage(new PaiementAnnuleDTO
                {
                    montant = pari.Mise,
                    idPari = pari.Id,
                    idUtilisateur = pari.UtilisateurId,
                }, "pari.annule", "pari.annule");
                Console.WriteLine($"Evenement non terminé supprimé, pari avec l'id {pari.Id} remboursé ");
            }
            else 
            {
                _pariRepository.DeletePari(pari);
                _pariOuvertService.DeletePariOuvertByIdEvenement(idEvenement);
                Console.WriteLine("Evenement terminé supprimé, les paris sont juste supprimés");
            }
        }
    }
}
