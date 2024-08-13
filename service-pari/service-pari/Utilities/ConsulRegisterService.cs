using Consul;
using Microsoft.Extensions.Options;

namespace service_pari.Utilities;

public class ConsulRegisterService : IHostedService
{
    private readonly IConsulClient _consulClient;
    private PariConfiguration _pariConfiguration;
    private ConsulConfiguration _consulConfiguration;
    private AgentServiceRegistration _serviceRegistration;

    public ConsulRegisterService(IConsulClient consulClient, 
        IOptions<PariConfiguration> pariConfiguration, 
        IOptions<ConsulConfiguration> consulConfiguration)
    {
        _consulClient = consulClient;
        _consulConfiguration = consulConfiguration.Value;
        _pariConfiguration = pariConfiguration.Value;
        this._serviceRegistration = new AgentServiceRegistration();
    }


    public async Task StartAsync(CancellationToken cancellationToken)
    {
        Uri uri = new Uri(_pariConfiguration.Url);
        //var serviceRegistration = new AgentServiceRegistration()
        //{
        //    Address = uri.Host,
        //    Name = _pariConfiguration.ServiceName,
        //    Port = uri.Port,
        //    ID = _pariConfiguration.ServiceName,
        //    Tags = new[] { _pariConfiguration.ServiceName }
        //};
        this._serviceRegistration.Address = uri.Host;
        this._serviceRegistration.Port = uri.Port;
        this._serviceRegistration.Name = _pariConfiguration.ServiceName;
        this._serviceRegistration.ID = _pariConfiguration.ServiceName;

        await _consulClient.Agent.ServiceDeregister(this._serviceRegistration.ID, cancellationToken); 
        await _consulClient.Agent.ServiceRegister(this._serviceRegistration, cancellationToken);
        
        
    }

    public async Task StopAsync(CancellationToken cancellationToken)
    {
        try
        {
            await _consulClient.Agent.ServiceDeregister(_serviceRegistration.ID, cancellationToken);
        }
        catch (Exception e)
        {
            Console.WriteLine($"------------> Consul Error : {e.Message}");
            throw;
        }
    }
}

public class PariConfiguration
{
    public string Url { get; set; }
    
    public string ServiceName { get; set; }
}

public class ConsulConfiguration
{
    public string Url { get; set; }
}