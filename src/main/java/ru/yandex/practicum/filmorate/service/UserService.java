package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.controller.model.user.UserDto;
import ru.yandex.practicum.filmorate.controller.model.user.UserRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface UserService extends BaseService<UserDto, UserRequest> {
    Set<UserDto> getFriends(Long id);

    Set<UserDto> getCommonFriends(Long id, Long otherId);

    boolean addFriend(Long id, Long otherId);

    boolean deleteFriend(Long id, Long otherId);
}


    public List<FilmDto> getRecommendations(Long userId) {
        List<Long> bestRepetitionUserIds = userStorage.getBestRepetitionUserIds(userId);

        if (bestRepetitionUserIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<FilmDto> recommendationsFilms = bestRepetitionUserIds.stream()
                .flatMap(userIdBestRep -> filmStorage.getRecommendations(userId, userIdBestRep).stream())
                .map(FilmMapper::mapToFilmDto)
                .toList();

        if (!recommendationsFilms.isEmpty()) {
            recommendationsFilms.stream()
                    .filter(filmDto -> !genreStorage.getForFilm(filmDto.getId()).isEmpty())
                    .forEach(filmDto -> filmDto.setGenres(
                                    genreStorage.getForFilm(filmDto.getId()).stream()
                                            .map(GenreMapper::mapToGenreDto)
                                            .toList()
                            )
                    );
        }
        return recommendationsFilms;
    }