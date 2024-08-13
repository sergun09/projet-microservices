package fr.orleans.serviceevenement.dtos;

import java.util.List;

public record PageDTO<T>(List<T> items, int currentPage, long totalItems, int totalPages )
{
}
