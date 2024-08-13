export interface PageDto<T> {
    items: T[];
    currentPage: number;
    totalItems: number;
    totalPages: number;
}