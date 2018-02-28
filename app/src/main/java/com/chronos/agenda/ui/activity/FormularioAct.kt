package com.chronos.agenda.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.chronos.agenda.R
import com.chronos.agenda.dao.AlunoDAO
import com.chronos.agenda.helper.FormularioHelper
import com.chronos.agenda.model.Aluno
import com.chronos.agenda.ui.service.AlunoService
import kotlinx.android.synthetic.main.activity_formulario.view.*
import java.io.File


/**
 * Created by John Vanderson M L on 25/02/2018.
 */
class FormularioAct: AppCompatActivity() {

    private val CODIGO_CAMERA = 567
    private var caminhoFoto = ""

    private val view by lazy {
        window.decorView
    }

    private val helper by lazy {
        FormularioHelper(this,Aluno());
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_formulario);


        val aluno = intent.getSerializableExtra("aluno") as? Aluno
        if(aluno !=null){
            helper.preencheFormulario(aluno);
        }
        val botao = view.formulario_botao_foto
        botao.setOnClickListener {
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            caminhoFoto = getExternalFilesDir(null).toString() + "/" + System.currentTimeMillis() + ".jpg"
            val arquivoFoto = File(caminhoFoto)
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto))
            startActivityForResult(intentCamera, CODIGO_CAMERA)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO_CAMERA) {
                helper.carregaImagem(caminhoFoto);
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_formulario, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

         when(item?.itemId){
             R.id.menu_formulario_ok->{
                 var aluno = helper.pegaAluno();
                 val dao = AlunoDAO(this)
                 if(!TextUtils.isEmpty(aluno.id)){
                     dao.altera(aluno)
                 }else{
                   aluno =  dao.insere(aluno)
                 }
                 dao.close()

                 AlunoService(this).enviarAluno(aluno)
                 Toast.makeText(this, "Aluno ${aluno.nome} salvo!", Toast.LENGTH_SHORT).show();
                 finish()
             }
         }
        return super.onOptionsItemSelected(item)
    }


}


