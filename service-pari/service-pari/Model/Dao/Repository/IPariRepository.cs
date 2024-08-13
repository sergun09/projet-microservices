using service_pari.Model.Dto;
using service_pari.Model.Entity;

namespace service_pari.Model.Dao.Repository;

public interface IPariRepository
{
    Task<Pari> GetPariByIdAsync(int pariId);
    Task<PagedResult<Pari>> GetParisAsync(int pageIndex, int pageSize);
    IReadOnlyList<Pari> GetParisByEvenementId(int idEvenement);
    Task<PagedResult<Pari>> GetParisByUserAsync(int utilisateurId, int pageIndex, int pageSize);
    Task AddPariAsync(Pari pari);
    void DeletePari(Pari pari);
    List<EventCountResult> GetNbParisByEvent();

    void DeleteParis(IReadOnlyList<Pari> paris);

}
