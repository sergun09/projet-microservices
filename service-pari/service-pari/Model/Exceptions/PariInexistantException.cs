using System.Runtime.Serialization;

namespace service_pari.Model.Exceptions
{
    [Serializable]
    internal class PariInexistantException : Exception
    {
        public PariInexistantException()
        {
        }

        public PariInexistantException(string? message) : base(message)
        {
        }

        public PariInexistantException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected PariInexistantException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}