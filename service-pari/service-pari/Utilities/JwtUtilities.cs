using System.IdentityModel.Tokens.Jwt;
using System.Net;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using Consul;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using Microsoft.IdentityModel.Tokens;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace service_pari.Utilities;

public class JwtUtilities
{
    public static async Task<RsaSecurityKey> GetPublicKey(IConfiguration configuration)
    {

        var consulUrl = configuration.GetSection("Consul")["Url"];
        ConsulClient client = new ConsulClient();
        client.Config.Address = new Uri(consulUrl);

        var keyPath = "config/application/publicKey";

        var rsa = RSA.Create();

        var queryResult = await client.KV.Get(keyPath);

        if (queryResult.StatusCode == HttpStatusCode.OK && queryResult.Response != null)
        {
            // Retrieve the base64-encoded value
            var h = queryResult.Response.Value;
            var rawKey = Encoding.UTF8.GetString(queryResult.Response.Value);

            var publicKey = rawKey
                .Replace("-----BEGIN PUBLIC KEY-----", "")
                .Replace("-----END PUBLIC KEY-----", "")
                .Replace("\n", "");

            byte[] encoded = Convert.FromBase64String(publicKey);

            rsa.ImportSubjectPublicKeyInfo(encoded, out _);
            Console.WriteLine("Clé récupéré");
            return new RsaSecurityKey(rsa);

        }

        return new RsaSecurityKey(rsa);
        
    }

    public static JwtSecurityToken DecodeJwt(string jwtoken) 
    {
        var tokenHandler = new JwtSecurityTokenHandler();
        return tokenHandler.ReadJwtToken(jwtoken);
    }

}