
using Microsoft.Extensions.Options;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using service_pari.Model.Dao.Repository;
using service_pari.Model.Dto;
using service_pari.Model.Service;
using System.Text;
using System.Text.Json;

namespace service_pari.Utilities.RabbitMQ;

public class RabbitMQPariOuvertConsumer : IHostedService
{
    private readonly IConnection _connection;
    private readonly IModel _channel;

    public IServiceScopeFactory _serviceScopeFactory;

    private readonly RabbitMQOptions _rabbitMQOptions;

    public RabbitMQPariOuvertConsumer(IOptions<RabbitMQOptions> rabbitMQOptions, IServiceScopeFactory serviceScopeFactory)
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

        //_channel.QueueDeclare(queue: "evenement.nouveau", exclusive: false);
        _channel.QueueBind("evenement.nouveau", "evenement.nouveau", "evenement.nouveau");
    }

    public Task StartAsync(CancellationToken cancellationToken)
    {
        var consumer = new EventingBasicConsumer(_channel);
        consumer.Received += (model, ea) =>
        {
            var body = ea.Body.ToArray();
            var message = Encoding.UTF8.GetString(body);
            Console.WriteLine("Message Received CHEFFFFF : {0}", message);
            var pariOuvertsDTO = JsonSerializer.Deserialize<PariOuvertDTO>(message);
            Console.WriteLine("TEST");
            using (IServiceScope scope = _serviceScopeFactory.CreateScope())
            {
                IPariOuvertService scopedProcessingService =
                    scope.ServiceProvider.GetRequiredService<IPariOuvertService>();
                scopedProcessingService.AddPariOuvert(pariOuvertsDTO.idEvenement, pariOuvertsDTO.dateEvenement);
            }
        };

        _channel.BasicConsume(queue: "evenement.nouveau",
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
