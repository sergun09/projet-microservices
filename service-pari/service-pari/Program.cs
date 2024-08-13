using Asp.Versioning;
using Consul;
using HealthChecks.UI.Client;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Diagnostics.HealthChecks;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using service_pari.Model.Dao;
using service_pari.Model.Dao.Repository;
using service_pari.Model.Service;
using service_pari.Utilities;
using service_pari.Utilities.RabbitMQ;

var builder = WebApplication.CreateBuilder(args);
var config = builder.Configuration;
config.AddJsonFile("appsettings.json", optional: false, reloadOnChange: true);
config.AddEnvironmentVariables();

// Add services to the container.
builder.Services.AddHealthChecks()
    .AddNpgSql(config.GetConnectionString("DefaultConnection"))
    .AddRabbitMQ(rabbitConnectionString : "amqps://guest:guest@rabbitmq:5672/vhost");

Console.WriteLine("---- Connexion BDD");
builder.Services.AddDbContext<PariContext>(
    options => options.UseNpgsql(config.GetConnectionString("DefaultConnection")));


builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();


Console.WriteLine("---- Connexion Consul");

builder.Services.AddSingleton<IConsulClient, ConsulClient>(provider =>
    new ConsulClient()
    {
        Config = { Address = new Uri(config.GetSection("Consul")["Url"]) }
    }
);

var MyAllowSpecificOrigins = "_myAllowSpecificOrigins";

builder.Services.AddCors(options =>
{
    options.AddPolicy(name: MyAllowSpecificOrigins,
                      policy =>
                      {
                          policy.WithOrigins("http://localhost:6020",
                                            "http://localhost:4200",
                                              "http://localhost:8080")
                          .AllowAnyMethod()
                          .AllowAnyHeader();
                      });
});

//var apiVersioningBuilder = builder.Services.AddApiVersioning(o =>
//{
//    o.AssumeDefaultVersionWhenUnspecified = true;
//    o.DefaultApiVersion = new ApiVersion(1, 0);
//    o.ReportApiVersions = true;
//    o.ApiVersionReader = new HeaderApiVersionReader("API-X-VERSION");
        
//});

//apiVersioningBuilder.AddApiExplorer(
//    options =>
//    {
//        options.GroupNameFormat = "'v'VVV";
//    });


builder.Services.Configure<RabbitMQOptions>(config.GetSection("RabbitMQ"));
builder.Services.Configure<PariConfiguration>(config.GetSection("Pari"));
builder.Services.Configure<ConsulConfiguration>(config.GetSection("CONSUL"));


builder.Services.AddSingleton<IHostedService, ConsulRegisterService>();


builder.Services.AddScoped<IPariRepository, PariRepository>();
builder.Services.AddScoped<IPariOuvertRepository, PariOuvertRepository>();

builder.Services.AddScoped<IPariService,PariService>();
builder.Services.AddScoped<IPariOuvertService, PariOuvertService>();

builder.Services.AddScoped<IRabbitMQSenderService, RabbitMQSenderService>();

RsaSecurityKey key = await JwtUtilities.GetPublicKey(config);
builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultSignInScheme = JwtBearerDefaults.AuthenticationScheme;
})
.AddJwtBearer(options =>
{
    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuer = true,
        ValidateAudience = false,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        ValidIssuer = "self",
        IssuerSigningKey = key,
    };
});

builder.Services.AddHostedService<RabbitMQPariOuvertConsumer>();
builder.Services.AddHostedService<RabbitMQResultatConsumer>();
builder.Services.AddHostedService<RabbitMQEvenementSupprimeConsumer>();

builder.Services.AddAuthorization();

var app = builder.Build();
app.UseHealthChecks("/health", new HealthCheckOptions 
{
    ResponseWriter = UIResponseWriter.WriteHealthCheckUIResponse
});

app.ApplyMigrations();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseRouting();
//app.UseHttpsRedirection();


app.UseCors(MyAllowSpecificOrigins);

app.UseAuthentication();

app.UseAuthorization();

app.MapControllers();

app.Run();
