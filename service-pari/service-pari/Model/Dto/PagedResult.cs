namespace service_pari.Model.Dto
{
    public class PagedResult<T>
    {
        public IReadOnlyList<T> items { get; set; }
        public int totalItems { get; set; }

        public int currentPage { get; set; }
        public int totalPages { get; set; }
    }
}
