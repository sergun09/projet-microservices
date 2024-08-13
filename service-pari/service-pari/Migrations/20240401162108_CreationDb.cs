using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;
using service_pari.Model.Entity;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace service_pari.Migrations
{
    /// <inheritdoc />
    public partial class CreationDb : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "PariOuverts",
                columns: table => new
                {
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    EvenementId = table.Column<int>(type: "integer", nullable: false),
                    DateLimite = table.Column<DateTime>(type: "timestamp without time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_PariOuverts", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Paris",
                columns: table => new
                {
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    UtilisateurId = table.Column<int>(type: "integer", nullable: false),
                    EvenementId = table.Column<int>(type: "integer", nullable: false),
                    TransactionId = table.Column<int>(type: "integer", nullable: false),
                    Mise = table.Column<decimal>(type: "numeric(18,2)", precision: 18, scale: 2, nullable: false),
                    Prediction = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Paris", x => x.Id);
                });

            migrationBuilder.InsertData(
                    table: "PariOuverts",
                    columns: new[] { "Id", "DateLimite", "EvenementId" },
                    values: new object[,]
                    {
                        { 1, new DateTime(2024, 04, 02, 14, 33, 0, 0, DateTimeKind.Utc), 1 },
                        { 2, new DateTime(2024, 04, 01, 13, 37, 0, 0, DateTimeKind.Utc), 2 },
                        { 3, new DateTime(2024, 04, 03, 15, 30, 0, 0, DateTimeKind.Utc), 3 }
                    });

            migrationBuilder.InsertData(
                table: "Paris",
                columns: new[] { "Id", "EvenementId", "Mise", "Prediction", "TransactionId", "UtilisateurId" },
                values: new object[,]
                {
                    { 1, 1, 20.00, "Equipe1", 0, 1 },
                    { 2, 1, 50.00, "Equipe2", 0, 1 },
                    { 3, 1, 85.00, "Nul", 0, 1 },
                    { 4, 2, 8.00, "Nul", 0, 2 },
                    { 5, 4, 55.00, "Equipe1", 0, 2 },
                    { 6, 4, 12.00, "Equipe2", 0, 2 },
                    { 7, 4, 155.00, "Nul", 0, 2 },
                });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "PariOuverts");

            migrationBuilder.DropTable(
                name: "Paris");
        }
    }
}