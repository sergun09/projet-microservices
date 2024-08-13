using service_pari.Model.Dto;
using service_pari.Model.Entity;

namespace service_pari.Model.Service
{
    public interface IPariOuvertService
    {
        PariOuvert AddPariOuvert(int idEvenement, int[] dateLimite);

        PariOuvert GetPariOuvert(int id);

        void DeletePariOuvert(int id);

        public void DeletePariOuvertByIdEvenement(int idEvenement);

    }
}
