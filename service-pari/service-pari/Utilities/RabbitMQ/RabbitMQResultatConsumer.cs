using Microsoft.Extensions.Options;
using RabbitMQ.Client.Events;
using RabbitMQ.Client;
using System.Text;
using System.Text.Json;
using service_pari.Model.Dto;
using service_pari.Model.Service;

namespace service_pari.Utilities.RabbitMQ;

public class RabbitMQResultatConsumer : IHostedService
{
    private readonly IConnection _connection;
    private readonly IModel _channel;

    public IServiceScopeFactory _serviceScopeFactory;

    private readonly RabbitMQOptions _rabbitMQOptions;

    public RabbitMQResultatConsumer(IOptions<RabbitMQOptions> rabbitMQOptions, IServiceScopeFactory serviceScopeFactory)
    {
        _rabbitMQOptions = rabbitMQOptions.Value;
        _serviceScopeFactory = serviceScopeFactory;


        var factory = new ConnectionFactory()
        {
            HostName = _rabbitMQOptions.HostName,
            Port = int.Parse(_rabbitMQOptions.Port),
            UserName = _rabbitMQOptions.UserName,
            Password = _rabbitMQOptions.Password
        };

        _connection = factory.CreateConnection();
        _channel = _connection.CreateModel();

        //_channel.QueueDeclare(queue: "evenement.resultat", exclusive: false);
        _channel.QueueBind("evenement.resultat", "evenement.resultat", "evenement.resultat");
    }

    public Task StartAsync(CancellationToken cancellationToken)
    {
        var consumer = new EventingBasicConsumer(_channel);
        consumer.Received += (model, ea) =>
        {
            var body = ea.Body.ToArray();
            var message = Encoding.UTF8.GetString(body);
            Console.WriteLine("Message Received CHEFFFFF : {0}", message);
            var pariOuvertsDTO = JsonSerializer.Deserialize<EvenementResultatDTO>(message);


            using (IServiceScope scope = _serviceScopeFactory.CreateScope())
            {
                IPariService scopedProcessingService =
                    scope.ServiceProvider.GetRequiredService<IPariService>();

                scopedProcessingService.verificationPari(pariOuvertsDTO);

            }
        };

        _channel.BasicConsume(queue: "evenement.resultat",
                              autoAck: true,
                              consumer: consumer);

        return Task.CompletedTask;
    }

    public Task StopAsync(CancellationToken cancellationToken)
    {
        _connection.Close();
        return Task.CompletedTask;
    }
}
