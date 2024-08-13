using Microsoft.Extensions.Options;
using Newtonsoft.Json;
using RabbitMQ.Client;
using System.Text;
using System.Threading.Channels;

namespace service_pari.Utilities.RabbitMQ;

public interface IRabbitMQSenderService
{
    void SendMessage<T>(T message, string exchange, string queueName);
}

public class RabbitMQSenderService : IRabbitMQSenderService
{
    private readonly RabbitMQOptions _rabbitMQOptions;

    public RabbitMQSenderService(IOptions<RabbitMQOptions> rabbitMQOptions)
    {
        _rabbitMQOptions = rabbitMQOptions.Value;
    }
    public void SendMessage<T>(T message, string exchange, string queueName)
    {
        var factory = new ConnectionFactory()
        {
            HostName = _rabbitMQOptions.HostName,
            Port = int.Parse(_rabbitMQOptions.Port),
            UserName = _rabbitMQOptions.UserName,
            Password = _rabbitMQOptions.Password
        };

        using (var connection = factory.CreateConnection())
        using (var channel = connection.CreateModel())
        {
            //channel.ExchangeDeclare(exchange: exchange, type: ExchangeType.Direct);
            channel.ExchangeBind(exchange, exchange, exchange);

            string jsonMessage = JsonConvert.SerializeObject(message);
            var body = Encoding.UTF8.GetBytes(jsonMessage);

            Console.WriteLine($"Json message envoyé : {jsonMessage}");

            Console.WriteLine($"Le message de type T : {message}");

            Console.WriteLine($"Le message de type T en ToString : {message.ToString}");

            Console.WriteLine($"Le message byte transformé en String  : {Encoding.UTF8.GetString(body)}");

            channel.BasicPublish(exchange: exchange,
                                 routingKey: exchange, 
                                 body: body);
            Console.WriteLine("MEEEEEEEEEEEEEEEEEEEESSSSAGE PARTIIIIIIIIIIIIIII pour l'exchange : " + exchange);
        }
    }
}

public class RabbitMQOptions
{
    public string HostName { get; set; }
    public string UserName { get; set; }
    public string Password { get; set; }
    public string Port { get; set; }
}
