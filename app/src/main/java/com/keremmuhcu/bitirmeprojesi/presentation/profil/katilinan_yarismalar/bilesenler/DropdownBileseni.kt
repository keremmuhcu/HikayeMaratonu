package com.keremmuhcu.bitirmeprojesi.presentation.profil.katilinan_yarismalar.bilesenler

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.keremmuhcu.bitirmeprojesi.R
import com.keremmuhcu.bitirmeprojesi.presentation.profil.katilinan_yarismalar.KatilinanYarismalarState
import com.keremmuhcu.bitirmeprojesi.presentation.profil.katilinan_yarismalar.KatilinanYarismalarViewModel
import com.keremmuhcu.bitirmeprojesi.ui.theme.AcikGri
import com.keremmuhcu.bitirmeprojesi.ui.theme.AnaRenkKoyu

@Composable
fun DropdownBileseni(
    katilinanYarismalarState: KatilinanYarismalarState,
    katilinanYarismalarViewModel: KatilinanYarismalarViewModel
) {
    IconButton(onClick = { katilinanYarismalarViewModel.setDropdownGoster(!katilinanYarismalarState.dropdownGoster) }) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "", tint = AcikGri)

        DropdownMenu(
            expanded = katilinanYarismalarState.dropdownGoster,
            onDismissRequest = { katilinanYarismalarViewModel.setDropdownGoster(false) }
        ) {
            DropdownMenuItem(
                text = {
                    Row {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(painter = painterResource(id = R.drawable.yukari_ok), contentDescription = "")
                            RadioButton(
                                selected = katilinanYarismalarState.siralamaYonu == "artan",
                                onClick = {
                                    katilinanYarismalarViewModel.setSiralamaYonu("artan")
                                    katilinanYarismalarViewModel.sirala()
                                }
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(painter = painterResource(id = R.drawable.asagi_ok), contentDescription = "")
                            RadioButton(
                                selected = katilinanYarismalarState.siralamaYonu == "azalan",
                                onClick = {
                                    katilinanYarismalarViewModel.setSiralamaYonu("azalan")
                                    katilinanYarismalarViewModel.sirala()
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = AnaRenkKoyu,
                                )
                            )
                        }
                    }
                },
                onClick = {

                }
            )

            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Derece")
                        if (katilinanYarismalarState.siralamaKriteri == "derece") {
                            Icon(painter = painterResource(id = R.drawable.accepted), contentDescription = "")
                        }
                    }
                },
                onClick = {
                    katilinanYarismalarViewModel.setSiralamaKriteri("derece")
                    katilinanYarismalarViewModel.setDropdownGoster(false)
                    katilinanYarismalarViewModel.sirala()
                }
            )

            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Oy")
                        if (katilinanYarismalarState.siralamaKriteri == "oy") {
                            Icon(painter = painterResource(id = R.drawable.accepted), contentDescription = "")
                        }
                    }
                },
                onClick = {
                    katilinanYarismalarViewModel.setSiralamaKriteri("oy")
                    katilinanYarismalarViewModel.setDropdownGoster(false)
                    katilinanYarismalarViewModel.sirala()
                }
            )

            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Tarih")
                        if (katilinanYarismalarState.siralamaKriteri == "tarih") {
                            Icon(painter = painterResource(id = R.drawable.accepted), contentDescription = "")
                        }
                    }
                },
                onClick = {
                    katilinanYarismalarViewModel.setSiralamaKriteri("tarih")
                    katilinanYarismalarViewModel.setDropdownGoster(false)
                    katilinanYarismalarViewModel.sirala()
                }
            )
        }
    }
}
