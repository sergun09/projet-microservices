using Microsoft.EntityFrameworkCore;
using service_pari.Model.Dao;
using service_pari.Model.Dto;
using service_pari.Model.Entity;
using System;

namespace service_pari.Model.Dao.Repository;

public class PariRepository : IPariRepository
{
    private readonly PariContext _context;

    public PariRepository(PariContext context)
    {
        _context = context;
    }

    public async Task<Pari> GetPariByIdAsync(int pariId)
    {
        return await _context.Paris.FirstOrDefaultAsync(p => p.Id == pariId);
    }

    public async Task<PagedResult<Pari>> GetParisAsync(int pageIndex, int pageSize)
    {
        var query = _context.Paris.AsQueryable();

        var totalItems = await query.CountAsync();
        var items = await query.Skip((pageIndex - 1) * pageSize)
                               .Take(pageSize)
                               .AsNoTracking()
                               .ToListAsync();

        var result = new PagedResult<Pari>
        {
            items = items,
            totalItems = totalItems,

        };
        return result;
    }

    public async Task<PagedResult<Pari>> GetParisByUserAsync(int utilisateurId, int pageIndex, int pageSize)
    {
        var query = _context.Paris.AsQueryable();

        var totalItems = await query
            .Where(p => p.UtilisateurId == utilisateurId)
            .CountAsync();
        var items = await query
            .Where(p => p.UtilisateurId == utilisateurId)
            .Skip((pageIndex - 1) * pageSize)
            .Take(pageSize)
            .AsNoTracking()
            .ToListAsync();

        var result = new PagedResult<Pari>
        {
            items = items,
            totalItems = totalItems
        };
        return result;
    }

    public async Task AddPariAsync(Pari pari)
    {
        await _context.Paris.AddAsync(pari);
        await _context.SaveChangesAsync();
    }

    public void DeletePari(Pari pari)
    {
        _context.Paris.Remove(pari);
        _context.SaveChanges();
    }

    public void DeleteParis(IReadOnlyList<Pari> paris)
    {
        _context.Paris.RemoveRange(paris);
        _context.SaveChanges();
    }

    public IReadOnlyList<Pari> GetParisByEvenementId(int idEvenement)
    {
        return _context.Paris
            .AsNoTracking()
            .Where(p => p.EvenementId == idEvenement)
            .ToList();   
    }

    public List<EventCountResult> GetNbParisByEvent()
    {
        //return _context.Database.SqlQueryRaw<EventCountResult>("SELECT PariOuverts.EvenementId, COUNT(Paris.EvenementId) FROM Paris RIGHT JOIN PariOuverts ON Paris.EvenementId = PariOuverts.EvenementId GROUP BY PariOuverts.EvenementId ORDER BY PariOuverts.EvenementId;"
        //    , []).ToList();
        return _context.Paris
            
            .GroupBy(p => p.EvenementId)
            .Select(p => new EventCountResult { idEvenement = p.Key, nbParis = p.Count() })
            .ToList();
    }
}
