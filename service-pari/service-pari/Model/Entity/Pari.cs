using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;

namespace service_pari.Model.Entity;

public class Pari
{
    public int Id { get; set; }

    [Required]
    public int UtilisateurId { get; set; }

    [Required]
    public int EvenementId { get; set; }

    [Required]
    public int TransactionId { get; set; }

    [Required]
    [Range(0, 100_000, ErrorMessage = "Votre mise ne peut pas être négative")]
    public double Mise { get; set; }

    [Required]
    public Prediction Prediction { get; set; }
}

public enum Prediction
{

    Nul = 0,
    Equipe1 = 1,
    Equipe2 = 2,
}


