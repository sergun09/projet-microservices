using service_pari.Model.Dto;
using service_pari.Model.Entity;

namespace service_pari.Model.Service;

public interface IPariService
{
    Task<Pari> GetPariByIdAsync(int pariId);
    Task<PagedResult<Pari>> GetParisAsync(int pageIndex, int pageSize);

    IReadOnlyList<Pari> GetParisByEvenementId(int idEvenement);
    Task<PagedResult<Pari>> GetParisByUserAsync(int utilisateurId, int pageIndex, int pageSize);
    Task<Pari> AddPariAsync(PariDTO pariDto, int utilisateurId);

    void DeletePari(Pari pari);

    Task verificationPari(EvenementResultatDTO resultatDTO);

    List<EventCountResult> GetNbParisByEvent();

    public void DeleteParis(int idEvenement);

}
