package com.example.inostudioTask.presentation.filmList

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.inostudioTask.R
import com.example.inostudioTask.domain.model.Film
import com.example.inostudioTask.presentation.Screen
import com.example.inostudioTask.presentation.filmList.components.FilmListItem

@Composable
fun FilmListScreen(
    navController: NavController,
    viewModel: FilmListViewModel = hiltViewModel(),
    context: Context
) {
    val state = viewModel.state.value
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            SearchBar(
                hint = context.getString(R.string.searchbar_hint),
                modifier = Modifier
                    .fillMaxWidth(),
                textState = viewModel.state.value.searchText
            ) { query ->

                if(query.isEmpty()) {
                    viewModel.refresh()
                } else {
                    viewModel.searchFilms(query = query)
                }
            }

            if(state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(5.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                @Suppress("UNCHECKED_CAST")
                items(state.data as List<Film>) { film ->
                    FilmListItem(
                        film = film,
                        onItemClick = {
                            navController.navigate(Screen.FilmReviewScreen.route + "/${film.id}")
                        }
                    )
                }
            }
        }

        if(state.error != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.Error,
                    contentDescription = null,
                    Modifier
                        .padding(5.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    text = context.getString(state.error as Int),
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Button(
                    onClick = {
                        viewModel.refresh()
                    }
                ) {
                    Text(
                        text = context.getString(R.string.refresh_string),
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    textState: String = "",
    onSearch: (String) -> Unit = {}
) {

    var text by remember {
        mutableStateOf(textState)
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    isHintDisplayed = text.isEmpty()

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = MaterialTheme.colors.secondary),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(MaterialTheme.colors.onSurface, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        )

        if(isHintDisplayed) {
            Text(
                text = hint,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}