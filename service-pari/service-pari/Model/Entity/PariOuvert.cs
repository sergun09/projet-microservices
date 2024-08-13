using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;

namespace service_pari.Model.Entity;

public class PariOuvert
{
    public int Id { get; set; }
    
    public int EvenementId { get; set; }

    [Required]
    [DataType(DataType.DateTime, ErrorMessage = "Il faut spécifier une date limite de pari !")]
    public DateTime DateLimite { get; set; }
}

