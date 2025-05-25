const API_MOVIES = "http://localhost:8080/api/movies";
const API_GENRES = "http://localhost:8080/api/genres";
const API_DIRECTORS = "http://localhost:8080/api/directors";

document.addEventListener("DOMContentLoaded", () => {
    const movieList = document.getElementById("movieList");
    const form = document.getElementById("movieForm");

    const movieId = document.getElementById("movieId");
    const title = document.getElementById("title");
    const description = document.getElementById("description");
    const releaseYear = document.getElementById("releaseYear");
    const genre = document.getElementById("genre");
    const director = document.getElementById("director");

    const filterGenre = document.getElementById("filterGenre");
    const filterDirector = document.getElementById("filterDirector");
    const searchTitle = document.getElementById("searchTitle");
    const searchDesc = document.getElementById("searchDesc");
    const clearFilters = document.getElementById("clearFilters");

    let genres = [];
    let directors = [];

    loadGenres();
    loadDirectors();
    loadMovies();

    form.addEventListener("submit", e => {
        e.preventDefault();

        const movie = {
            title: title.value.trim(),
            description: description.value.trim(),
            releaseYear: +releaseYear.value,
            genre: { id: parseInt(genre.value) },
            director: { id: parseInt(director.value) }
        };

        const id = movieId.value;
        const method = id ? "PUT" : "POST";
        const url = id ? `${API_MOVIES}/${id}` : API_MOVIES;

        fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(movie)
        })
            .then(res => {
                if (!res.ok) return res.text().then(text => { throw new Error(text || res.status); });
                return res.json();
            })
            .then(() => {
                form.reset();
                movieId.value = "";
                loadMovies();
            })
            .catch(err => {
                console.error("Błąd zapisu filmu:", err);
                alert("Błąd podczas zapisu filmu. Szczegóły w konsoli.");
            });
    });

    function loadGenres() {
        fetch(API_GENRES)
            .then(res => res.json())
            .then(data => {
                genres = data;
                genre.innerHTML = data.map(g => `<option value="${g.id}">${g.name}</option>`).join("");
                filterGenre.innerHTML = `<option value="">-- Filter genre --</option>` +
                    data.map(g => `<option value="${g.name}">${g.name}</option>`).join("");
            });
    }

    function loadDirectors() {
        fetch(API_DIRECTORS)
            .then(res => res.json())
            .then(data => {
                directors = data;
                director.innerHTML = data.map(d => `<option value="${d.id}">${d.fullName}</option>`).join("");
                filterDirector.innerHTML = `<option value="">-- Filter director --</option>` +
                    data.map(d => `<option value="${d.fullName}">${d.fullName}</option>`).join("");
            });
    }

    function loadMovies() {
        fetch(API_MOVIES)
            .then(res => res.json())
            .then(displayMovies)
            .catch(err => console.error("Błąd ładowania filmów:", err));
    }

    function displayMovies(movies) {
        movieList.innerHTML = "";
        movies.forEach(m => {
            const li = document.createElement("li");
            li.className = "list-group-item d-flex justify-content-between align-items-center";
            li.innerHTML = `
                <div>
                    <strong>${m.title}</strong> (${m.releaseYear})<br>
                    <small>${m.description}</small><br>
                    <em>${m.genre?.name || ''} / ${m.director?.fullName || ''}</em>
                </div>
                <div>
                    <button class="btn btn-sm btn-warning me-2" onclick="editMovie(${m.id})">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteMovie(${m.id})">Delete</button>
                </div>`;
            movieList.appendChild(li);
        });
    }

    window.deleteMovie = function(id) {
        fetch(`${API_MOVIES}/${id}`, { method: "DELETE" })
            .then(loadMovies)
            .catch(err => console.error("Błąd usuwania filmu:", err));
    };

    window.editMovie = function(id) {
        fetch(`${API_MOVIES}/${id}`)
            .then(res => res.json())
            .then(movie => {
                movieId.value = movie.id;
                title.value = movie.title;
                description.value = movie.description;
                releaseYear.value = movie.releaseYear;
                genre.value = movie.genre?.id || "";
                director.value = movie.director?.id || "";
            })
            .catch(err => console.error("Błąd edycji filmu:", err));
    };

    // Filtrowanie
    filterGenre.addEventListener("change", applyFilters);
    filterDirector.addEventListener("change", applyFilters);
    searchTitle.addEventListener("input", applyFilters);
    searchDesc.addEventListener("input", applyFilters);

    clearFilters.addEventListener("click", () => {
        filterGenre.value = "";
        filterDirector.value = "";
        searchTitle.value = "";
        searchDesc.value = "";
        applyFilters();
    });

    function applyFilters() {
        fetch(API_MOVIES)
            .then(res => res.json())
            .then(movies => {
                let filtered = movies;

                const selectedGenre = filterGenre.value;
                const selectedDirector = filterDirector.value;
                const titleQuery = searchTitle.value.trim().toLowerCase();
                const descQuery = searchDesc.value.trim().toLowerCase();

                if (selectedGenre) {
                    filtered = filtered.filter(m => m.genre?.name === selectedGenre);
                }

                if (selectedDirector) {
                    filtered = filtered.filter(m => m.director?.fullName === selectedDirector);
                }

                if (titleQuery) {
                    filtered = filtered.filter(m => m.title.toLowerCase().includes(titleQuery));
                }

                if (descQuery) {
                    filtered = filtered.filter(m => m.description.toLowerCase().includes(descQuery));
                }

                displayMovies(filtered);
            })
            .catch(err => console.error("Błąd filtrowania:", err));
    }

    window.sortAsc = function() {
        fetch(`${API_MOVIES}/sorted/year/asc`)
            .then(res => res.json())
            .then(displayMovies);
    };

    window.sortDesc = function() {
        fetch(`${API_MOVIES}/sorted/year/desc`)
            .then(res => res.json())
            .then(displayMovies);
    };
});
