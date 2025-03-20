package com.example.boardGamesStore.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.boardGamesStore.R
import com.example.boardGamesStore.data.entity.BoardGame

class BoardGameDetailsDialog : DialogFragment() {
    private lateinit var boardGame: BoardGame

    companion object {
        private const val ARG_BOARD_GAME = "board_game"

        fun newInstance(boardGame: BoardGame): BoardGameDetailsDialog {
            val fragment = BoardGameDetailsDialog()
            val args = Bundle()
            args.putParcelable(ARG_BOARD_GAME, boardGame)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

        boardGame = arguments?.getParcelable(ARG_BOARD_GAME) ?: throw IllegalArgumentException("BoardGame must be provided")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_board_game_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Важливо: встановлюємо ширину діалогу з відступами
        dialog?.window?.let { window ->
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt() // 85% ширини екрану
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

            // Додатково встановлюємо відступи для діалогу
            val params = window.attributes
            params.x = 0
            params.y = 0
            window.attributes = params
        }

        val imageView: ImageView = view.findViewById(R.id.iv_game_image)
        val nameTextView: TextView = view.findViewById(R.id.tv_game_name)
        val priceTextView: TextView = view.findViewById(R.id.tv_game_price)
        val descriptionTextView: TextView = view.findViewById(R.id.tv_game_description)
        val closeButton: Button = view.findViewById(R.id.btn_close)

        nameTextView.text = boardGame.name
        priceTextView.text = String.format("₴%.2f", boardGame.price)
        descriptionTextView.text = boardGame.description

        // Сначала установим placeholder
        imageView.setImageResource(R.drawable.placeholder_game)

        // Потім намагаємось завантажити зображення, якщо воно доступне
        val imageUrl = boardGame.imageUrl
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            try {
                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_game)
                    .error(R.drawable.placeholder_game)
                    .into(imageView)
            } catch (e: Exception) {
                // Якщо щось пішло не так, залишаємо placeholder
                imageView.setImageResource(R.drawable.placeholder_game)
            }
        }

        closeButton.setOnClickListener {
            dismiss()
        }
    }
}