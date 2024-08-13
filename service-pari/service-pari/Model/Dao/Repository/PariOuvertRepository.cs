using Microsoft.EntityFrameworkCore;
using service_pari.Model.Dto;
using service_pari.Model.Entity;

namespace service_pari.Model.Dao.Repository;

public class PariOuvertRepository : IPariOuvertRepository
{
    private readonly PariContext _context;

    public PariOuvertRepository(PariContext context)
    {
        _context = context;
    }



    public PariOuvert AddPariOuvert(int idEvenement, DateTime dateLimite)
    {
        PariOuvert pariOuvert = new PariOuvert() 
        {
            EvenementId = idEvenement,
            DateLimite = dateLimite
        };
        _context.Add(pariOuvert);
        _context.SaveChanges();
        return pariOuvert;
    }

    public void DeletePariOuvert(int id)
    {
        PariOuvert pariOuvert = _context.PariOuverts.Where(p => p.Id == id).FirstOrDefault();
        _context.PariOuverts.Remove(pariOuvert);
        _context.SaveChanges();
    }

    public void DeletePariOuvertByIdEvenement(int idEvenement)
    {
        _context.PariOuverts.Where(p => p.EvenementId == idEvenement).ExecuteDelete();
    }

    public PariOuvert GetPariOuvert(int id)
    {
        return _context.PariOuverts.Where(p => p.EvenementId == id).FirstOrDefault();
    }
}
