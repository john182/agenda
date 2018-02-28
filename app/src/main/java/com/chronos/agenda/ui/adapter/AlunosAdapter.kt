package com.chronos.agenda.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.chronos.agenda.R
import com.chronos.agenda.model.Aluno
import kotlinx.android.synthetic.main.list_item.view.*


/**
 * Created by John Vanderson M L on 25/02/2018.
 */
class AlunosAdapter(private val context: Context,private val alunos:List<Aluno>) : BaseAdapter() {



    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        val aluno = alunos[position]
        val view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)

        if(!TextUtils.isEmpty(aluno.caminhoFoto)){
            val bitmap = BitmapFactory.decodeFile(aluno.caminhoFoto)
            val bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
            view.item_foto.setImageBitmap(bitmapReduzido)
            view.item_foto.scaleType = ImageView.ScaleType.FIT_XY
        }

        with(view){
            item_nome.text = aluno.nome
            item_telefone.text = aluno.endereco

        }

        return view
    }

    override fun getItem(position: Int): Aluno {
        return alunos[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return alunos.size
    }
}