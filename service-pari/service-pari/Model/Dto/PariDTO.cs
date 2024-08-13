using System.ComponentModel.DataAnnotations;

namespace service_pari.Model.Dto;

public record PariDTO(
    [Required(ErrorMessage = "La mise requiert un evenement")]
    int EvenementId,
    [Required]
    int TransactionId,
    [Required(ErrorMessage = "La mise ne peut pas être nul")]
    [Range(0, int.MaxValue, ErrorMessage = "Votre mise ne peut pas être négative")]
    double Mise,
    [Required(ErrorMessage = "Il faut choisir une option de pari !")]
    string Prediction);

