using Microsoft.EntityFrameworkCore;
using service_pari.Model.Dao;

namespace service_pari.Utilities;

public static class MigrationExtensions
{
    public static void ApplyMigrations(this IApplicationBuilder app) 
    {
        using IServiceScope scope = app.ApplicationServices.CreateScope();

        using PariContext dbContext =
            scope.ServiceProvider.GetService<PariContext>();

        dbContext.Database.Migrate();
    }
}
