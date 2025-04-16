package com.example.spygame.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spygame.R
import com.example.spygame.ui.theme.gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.rules),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        },
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_main_screen),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
    ) { innerPadding ->
        GameInstructions(modifier = Modifier, innerPadding = innerPadding)
    }
}

@Composable
fun GameInstructions(modifier: Modifier, innerPadding: PaddingValues) {

    val gameInstructions = buildAnnotatedString {
        pushStyle(
            ParagraphStyle(
                lineHeight = 24.sp
            )
        )

        // مقدمه
        pushStyle(
            SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = gold
            )
        )
        append(stringResource(R.string.introduction))
        pop()

        append(stringResource(R.string.in_this_game_one_person_is_chosen_as_the_spy_and_the_remaining_players_must_try_to_identify_the_spy))
        append(stringResource(R.string.the_spy_in_turn_must_try_not_to_reveal_themselves_while_attempting_to_guess_the_location))

        // نحوه بازی
        pushStyle(
            SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = gold // رنگ طلایی برای تیترها
            )
        )
        append("\n\n") // فاصله برای خوانایی بهتر
        append(stringResource(R.string.how_to_play))
        pop()

        append(stringResource(R.string._1_the_number_of_players_and_game_time_are_determined))
        append(stringResource(R.string._2_a_word_location_profession_food_etc_is_randomly_selected_and_shown_to_all_players_except_the_spy))
        append(stringResource(R.string._3_players_take_turns_passing_the_device_around_to_view_the_location))
        append(stringResource(R.string._4_the_game_starts_with_players_asking_each_other_questions_at_first_each_player_asks_a_question_about_the_location_to_the_player_next_to_them))
        append(stringResource(R.string._5_the_questions_should_be_phrased_in_a_way_that_the_spy_cannot_easily_guess_the_location))
        append(stringResource(R.string._6_if_the_spy_is_identified_the_players_win_if_the_spy_can_guess_the_location_or_is_not_identified_the_spy_wins))

        // هدف بازی
        pushStyle(
            SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = gold // رنگ طلایی برای تیترها
            )
        )
        append("\n\n") // فاصله برای خوانایی بهتر
        append(stringResource(R.string.objective_of_the_game))
        pop()

        append(stringResource(R.string.the_objective_of_the_players_is_to_identify_the_spy))
        append(stringResource(R.string.the_objective_of_the_spy_is_to_remain_undetected_and_guess_the_location))

        // پایان بازی
        pushStyle(
            SpanStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = gold // رنگ طلایی برای تیترها
            )
        )
        append("\n\n") // فاصله برای خوانایی بهتر
        append(stringResource(R.string.at_the_end_of_the_game_if_the_spy_identifies_themselves_and_guesses_the_location_they_win))
        append(stringResource(R.string.if_the_players_correctly_identify_the_spy_they_win))
    }

    LazyColumn(
        modifier = modifier
            .padding(innerPadding),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = gameInstructions,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

