using Microsoft.IdentityModel.Tokens;
using System.Text;
using Newtonsoft.Json;
using System.Security.Cryptography;
using Consul;

namespace Securities
{

    // Classe singleton
    public class JWTDecoder
    {
        private string consulHost = Environment.GetEnvironmentVariable("CONSUL_HOST") ?? "localhost";
        private string consulPort = Environment.GetEnvironmentVariable("CONSUL_PORT") ?? "8500";
        private SecurityKey securityKey;

        private static JWTDecoder instance = null;
        private JWTDecoder()
        {
            // Initialisation du singleton
            this.securityKey = GetCertificateAsync().Result;
        }
        private static object lockThis = new object();

        public static JWTDecoder GetInstance
        {
            get
            {
                lock (lockThis)
                {
                    if (instance == null)
                        instance = new JWTDecoder();

                    return instance;
                }
            }
        }

        private async Task<SecurityKey> GetCertificateAsync()
        {
            // URL du service qui fournit le certificat contenant la clé publique RSA
            string certServiceUrl = "http://" + this.consulHost + ":" + this.consulPort + "/v1/kv/config/application/publicKey";

            using (HttpClient client = new HttpClient())
            {
                // Effectuer une requête GET vers le service pour récupérer le certificat
                try
                {
                    HttpResponseMessage response = await client.GetAsync(certServiceUrl);

                    if (response.IsSuccessStatusCode)
                    {
                        // Lire le contenu de la réponse (le certificat)
                        string jsonContent = await response.Content.ReadAsStringAsync();
                        List<KVPair> keyValueItems = JsonConvert.DeserializeObject<List<KVPair>>(jsonContent);

                        // Extraire les données du premier élément de keyValueItems
                        KVPair firstItem = keyValueItems.FirstOrDefault();
                        if (firstItem != null)
                        {
                            // Décodage 

                            // Convertir les bytes en une chaîne de caractères
                            string decodedKeyString = Encoding.UTF8.GetString(firstItem.Value);

                            // Supprimer les en-têtes PEM
                            string publicKeyPEM = decodedKeyString
                                .Replace("-----BEGIN PUBLIC KEY-----", "")
                                .Replace("-----END PUBLIC KEY-----", "")
                                .Replace("\n", "");

                            // Convertir la chaîne de caractères en un tableau de bytes (byte[])
                            byte[] encoded = Convert.FromBase64String(publicKeyPEM);

                            var rsa = RSA.Create();
                            rsa.ImportSubjectPublicKeyInfo(encoded, out _);
                            Console.WriteLine("Clé récupéré");
                            return new RsaSecurityKey(rsa);
                        }
                        else
                        {
                            // Gérer le cas où keyValueItems est vide
                            throw new InvalidOperationException("No items found in the response.");
                        }
                    }
                    else
                    {
                        throw new HttpRequestException($"Failed to retrieve certificate. Status code: {response.StatusCode}");
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                    throw new HttpRequestException("Erreur");
                }


            }
        }

        public SecurityKey GetSecurityKey()
        {
            // Méthode du singleton
            return this.securityKey;
        }
    }
}

