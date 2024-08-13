namespace service_pari.Model.Dto;

public class PaiementAnnuleDTO
{
    public double montant { get; init; }

    public int idPari { get; init; }
        
    public int idUtilisateur { get; init; }
}
