using Microsoft.EntityFrameworkCore;
using service_pari.Model.Entity;

namespace service_pari.Model.Dao;

public class PariContext : DbContext
{
    public PariContext(DbContextOptions<PariContext> options) : base(options)
    {
        AppContext.SetSwitch("Npgsql.EnableLegacyTimestampBehavior", true);
    }



    public DbSet<Pari> Paris { get; set; }

    public DbSet<PariOuvert> PariOuverts { get; set; }

    

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder
            .Entity<Pari>()
            .Property(p => p.Prediction)
            .HasConversion<string>();


        modelBuilder.Entity<Pari>().HasData(new List<Pari>()
        {
            new Pari(){ Id = 1, EvenementId= 1, Mise = 20.00 , UtilisateurId = 1, Prediction = Prediction.Equipe1, TransactionId = 0 },
            new Pari(){ Id = 2, EvenementId= 1, Mise = 50.00 , UtilisateurId = 1, Prediction = Prediction.Equipe2, TransactionId = 0 },
            new Pari(){ Id = 3, EvenementId= 1, Mise = 85.00 , UtilisateurId = 1, Prediction = Prediction.Nul, TransactionId = 0 },
            new Pari(){ Id = 4, EvenementId= 2, Mise = 8.00 , UtilisateurId = 2, Prediction = Prediction.Nul, TransactionId = 0 },
            new Pari(){ Id = 5, EvenementId= 4, Mise = 55.00 , UtilisateurId = 2, Prediction = Prediction.Equipe2, TransactionId = 0 },
            new Pari(){ Id = 6, EvenementId= 4, Mise = 12.00 , UtilisateurId = 2, Prediction = Prediction.Nul, TransactionId = 0 },
            new Pari(){ Id = 7, EvenementId= 4, Mise = 150.00 , UtilisateurId = 2, Prediction = Prediction.Nul, TransactionId = 0 },
        });

    }
}

