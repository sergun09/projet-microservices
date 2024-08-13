using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;
using service_pari.Model.Dao;

#nullable disable

namespace service_pari.Migrations
{
    [DbContext(typeof(PariContext))]
    [Migration("20240401162108_CreationDb")]
    partial class CreationDb
    {
        /// <inheritdoc />
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "8.0.1")
                .HasAnnotation("Relational:MaxIdentifierLength", 63);

            NpgsqlModelBuilderExtensions.UseIdentityByDefaultColumns(modelBuilder);

            modelBuilder.Entity("service_pari.Model.Entity.Pari", b =>
            {
                b.Property<int>("Id")
                    .ValueGeneratedOnAdd()
                    .HasColumnType("integer");

                NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<int>("Id"));

                b.Property<int>("EvenementId")
                    .HasColumnType("integer");

                b.Property<decimal>("Mise")
                    .HasPrecision(18, 2)
                    .HasColumnType("numeric(18,2)");

                b.Property<string>("Prediction")
                    .IsRequired()
                    .HasColumnType("text");

                b.Property<int>("TransactionId")
                    .HasColumnType("integer");

                b.Property<int>("UtilisateurId")
                    .HasColumnType("integer");

                b.HasKey("Id");

                b.ToTable("Paris");

                b.HasData(
                    new
                    {
                        Id = 1,
                        EvenementId = 1,
                        Mise = 20.00m,
                        Prediction = "Equipe1",
                        TransactionId = 0,
                        UtilisateurId = 1
                    },
                    new
                    {
                        Id = 2,
                        EvenementId = 2,
                        Mise = 50.00m,
                        Prediction = "Equipe2",
                        TransactionId = 0,
                        UtilisateurId = 1
                    },
                    new
                    {
                        Id = 3,
                        EvenementId = 2,
                        Mise = 8.00m,
                        Prediction = "Nul",
                        TransactionId = 0,
                        UtilisateurId = 2
                    });
            });

            modelBuilder.Entity("service_pari.Model.Entity.PariOuvert", b =>
            {
                b.Property<int>("Id")
                    .ValueGeneratedOnAdd()
                    .HasColumnType("integer");

                NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<int>("Id"));

                b.Property<DateTime>("DateLimite")
                    .HasColumnType("timestamp without time zone");

                b.Property<int>("EvenementId")
                    .HasColumnType("integer");

                b.HasKey("Id");

                b.ToTable("PariOuverts");

                b.HasData(
                    new
                    {
                        Id = 1,
                        DateLimite = new DateTime(2024, 5, 15, 0, 0, 0, 0, DateTimeKind.Unspecified),
                        EvenementId = 1
                    },
                    new
                    {
                        Id = 2,
                        DateLimite = new DateTime(2024, 4, 23, 0, 0, 0, 0, DateTimeKind.Unspecified),
                        EvenementId = 2
                    },
                    new
                    {
                        Id = 3,
                        DateLimite = new DateTime(2024, 4, 19, 0, 0, 0, 0, DateTimeKind.Unspecified),
                        EvenementId = 2
                    });
            });
#pragma warning restore 612, 618
        }
    }
}