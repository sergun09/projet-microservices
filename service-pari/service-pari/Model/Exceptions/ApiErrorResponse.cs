namespace service_pari.Model.Exceptions;

public class ApiErrorResponse
{
    public List<string> errors { get; set; } = new();

    public ApiErrorResponse(string message)
    {
        this.errors.Add(message);
    }
}
