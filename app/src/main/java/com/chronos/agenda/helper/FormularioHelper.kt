package com.chronos.agenda.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.widget.ImageView
import com.chronos.agenda.extensoes.intToBigDecimal
import com.chronos.agenda.model.Aluno
import com.chronos.agenda.ui.activity.FormularioAct
import kotlinx.android.synthetic.main.activity_formulario.view.*


/**
 * Created by John Vanderson M L on 26/02/2018.
 */
class FormularioHelper(activity: FormularioAct,  aluno:Aluno) {

    private val view = activity.window.decorView
    private val campoNome = view.formulario_nome
    private val campoEndereco = view.formulario_endereco
    private val campoTelefone = view.formulario_telefone
    private val campoSite = view.formulario_site
    private val campoNota = view.formulario_nota
    private val campoFoto = view.formulario_foto
    private var aluno : Aluno = aluno

    fun pegaAluno(): Aluno {
        var caminhoFoto = ""
        if(campoFoto.tag!=null){
            caminhoFoto = campoFoto.tag as String
        }
        aluno.nome = campoNome.text.toString()
        aluno.endereco = campoEndereco.text.toString()
        aluno.telefone = campoTelefone.text.toString()
        aluno.site = campoSite.text.toString()
        aluno.nota = campoNota.progress.intToBigDecimal()
        aluno.caminhoFoto = caminhoFoto

        return aluno
    }

    fun preencheFormulario(aluno: Aluno) {
        campoNome.setText(aluno.nome)
        campoEndereco.setText(aluno.endereco)
        campoTelefone.setText(aluno.telefone)
        campoSite.setText(aluno.site)
        campoNota.progress = aluno.nota.toInt()
        if(!TextUtils.isEmpty(aluno.caminhoFoto)){
            carregaImagem(aluno.caminhoFoto)
        }

        this.aluno = aluno
    }

    fun carregaImagem(caminhoFoto: String) {
        val bitmap = BitmapFactory.decodeFile(caminhoFoto)
        val bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
        campoFoto.setImageBitmap(bitmapReduzido)
        campoFoto.scaleType = ImageView.ScaleType.FIT_XY
        campoFoto.tag = caminhoFoto

    }
}