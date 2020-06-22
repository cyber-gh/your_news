package dev.skyit.yournews.ui.main.settings

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.yournews.BaseFragment
import dev.skyit.yournews.api.models.CountryFilter
import dev.skyit.yournews.databinding.SettingsFragmentBinding
import dev.skyit.yournews.repository.preferences.IUserPreferences
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {
    private lateinit var binding: SettingsFragmentBinding

    @Inject
    protected lateinit var userPreferences: IUserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.slide_bottom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.materialToolbar3.onBackPressed {
            findNavController().navigateUp()
        }
        binding.materialToolbar3.setTitle("Settings")


        binding.miniCardsSwitch.isChecked = userPreferences.useMiniCards
        binding.notificationsSwitch.isChecked = userPreferences.getNotifications

        binding.miniCardsSwitch.setOnCheckedChangeListener { compoundButton, b ->
            userPreferences.useMiniCards = b
        }

        binding.notificationsSwitch.setOnCheckedChangeListener { compoundButton, b ->
            userPreferences.getNotifications = b
        }

        binding.prefferredCountry.setTitle("Preferred country")
        val countryName = CountryFilter.fromString(userPreferences.preferredCountry).longName()
        binding.prefferredCountry.setSubtitle(countryName)

        binding.prefferredCountry.setOnClickListener {
           requireContext().pick(
               msg = "Selected your country",
               choices = CountryFilter.values(),
               chosen = CountryFilter.fromString(userPreferences.preferredCountry),
               stringifier = {
                 longName()
               },
               didPick = {
                   if (it != null) {
                       userPreferences.preferredCountry = it.toString()
                       binding.prefferredCountry.setSubtitle(it.longName())
                   }
               })
        }

    }


}


fun <T>Context.pick(msg: String,
                    choices: Array<T>,
                    chosen: T? = null,
                    stringifier: T.() -> String = {
                        toString()
                    },
                    didPick: (T?) -> Unit)  {
    var selectedItem = choices.indexOf(chosen)
    AlertDialog.Builder(this)
        .setTitle(msg)
        .setSingleChoiceItems(
            choices.map { it.stringifier() }.toTypedArray(),
            selectedItem
        ) { dialogInterface, i ->
            selectedItem = i
        }
        .setPositiveButton("OK") { dialog, which ->
            didPick(choices.getOrNull(selectedItem))
        }.show()
}