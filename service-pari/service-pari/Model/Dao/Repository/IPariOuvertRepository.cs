using service_pari.Model.Dto;
using service_pari.Model.Entity;

namespace service_pari.Model.Dao.Repository;

public interface IPariOuvertRepository
{

    PariOuvert GetPariOuvert(int id);

    PariOuvert AddPariOuvert(int idEvenement, DateTime dateLimite);

    void DeletePariOuvert(int id);
    void DeletePariOuvertByIdEvenement(int idEvenement);
}
