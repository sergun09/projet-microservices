using service_pari.Model.Dao.Repository;
using service_pari.Model.Dto;
using service_pari.Model.Entity;
using service_pari.Utilities.RabbitMQ;

namespace service_pari.Model.Service
{
    public class PariOuvertService : IPariOuvertService
    {
        private readonly IPariOuvertRepository _pariOuvertRepository;


        public PariOuvertService(IPariOuvertRepository pariIOuvertRepository)
        {
            _pariOuvertRepository = pariIOuvertRepository;
        }

        public PariOuvert AddPariOuvert(int idEvenement, int[] dateLimite)
        {
            Console.WriteLine($"Resolve de la date en cours");
            DateTime dt = new DateTime(
                dateLimite[0], 
                dateLimite[1], 
                dateLimite[2], 
                dateLimite[3],
                dateLimite[4],
                0,0, DateTimeKind.Utc);
            Console.WriteLine("Date du pari OUVERTS : " + dt);
            return _pariOuvertRepository.AddPariOuvert(idEvenement, dt);
        }

        public void DeletePariOuvert(int id)
        {
            _pariOuvertRepository.DeletePariOuvert(id);
        }

        public void DeletePariOuvertByIdEvenement(int idEvenement)
        {
            _pariOuvertRepository.DeletePariOuvertByIdEvenement(idEvenement);
        }

        public PariOuvert GetPariOuvert(int id)
        {
            return _pariOuvertRepository.GetPariOuvert(id);
        }
    }
}
