package com.example.inostudioTask.presentation.actorReview.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.inostudioTask.R
import com.example.inostudioTask.data.remote.dto.Actor
import com.example.inostudioTask.data.remote.dto.Film
import com.example.inostudioTask.data.remote.dto.profilePathUrl
import com.example.inostudioTask.data.remote.dto.url
import com.example.inostudioTask.presentation.common.components.LikeButton
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@ExperimentalPagerApi
@Composable
fun ActorReviewSuccessScreen(
    actor: Actor,
    onFavoriteClick: (Actor) -> Unit,
    onFilmClick: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileImage(actor)

        Spacer(modifier = Modifier.height(2.dp))

        ActorInformation(actor)

        Spacer(modifier = Modifier.height(2.dp))

        ActorPhotos(actor)

        Spacer(modifier = Modifier.height(2.dp))

        StaredMovies(actor) { onFilmClick(it) }

        Spacer(modifier = Modifier.height(2.dp))

        LikeButton(isInDatabase = actor.isInDatabase!!) {
            onFavoriteClick(actor)
        }
    }
}

@Composable
private fun StaredMovies(
    actor: Actor,
    onFilmClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .height(300.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onSurface),
        backgroundColor = MaterialTheme.colors.background,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(2.dp)) {
            Text(
                text = stringResource(R.string.films),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(2.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(5.dp)
            ) {
                actor.movies?.results?.let { it ->
                    items(it.count()) { index ->
                        FilmItem(film = it[index]) { id -> onFilmClick(id) }
                        Spacer(modifier = Modifier.padding(5.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FilmItem(
    film: Film,
    onFilmClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable { onFilmClick(film.id) }
    ) {
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(225.dp),
            backgroundColor = MaterialTheme.colors.background,
            elevation = 3.dp,
            shape = MaterialTheme.shapes.small
        ) {
            Image(
                painter =
                if (film.posterPath.isNullOrEmpty()) {
                    rememberImagePainter(data = R.drawable.not_found_image)
                } else {
                    rememberImagePainter(film.imageUrl(film.posterPath))
                },
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillHeight
            )
        }

        Text(
            text = film.originalTitle,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@ExperimentalPagerApi
@Composable
private fun ActorPhotos(actor: Actor) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .height(300.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onSurface),
        backgroundColor = MaterialTheme.colors.background,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(2.dp)) {
            Text(
                text = stringResource(R.string.posters),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(2.dp)
            )

            actor.imageList?.images?.let {
                HorizontalPager(
                    count = it.count(),
                    itemSpacing = 4.dp,
                    contentPadding = PaddingValues(4.dp)
                ) { page ->
                    Card(
                        modifier = Modifier
                            .height(280.dp),
                        backgroundColor = MaterialTheme.colors.background,
                        elevation = 3.dp,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Image(
                            painter = rememberImagePainter(actor.imageList.images[page].url()),
                            contentDescription = null,
                            alignment = Alignment.Center,
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActorInformation(actor: Actor) {
    Card(
        modifier = Modifier.padding(2.dp),
        elevation = 4.dp,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onSurface),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(Modifier.padding(3.dp)) {
            Text(
                text = actor.name,
                style = MaterialTheme.typography.h1,
                fontSize = 27.sp,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = actor.biography ?: "",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
private fun ProfileImage(actor: Actor) {
    Card(
        modifier = Modifier
            .height(400.dp)
            .width(300.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 3.dp,
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onSurface)
    ) {
        Image(
            painter = rememberImagePainter(actor.profilePathUrl()),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
    }
}