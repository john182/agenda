package com.chronos.agenda.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.chronos.agenda.R
import com.chronos.agenda.api.AgendaAPI
import com.chronos.agenda.dao.AlunoDAO
import com.chronos.agenda.model.Aluno
import com.chronos.agenda.tasks.EnviaAlunosTask
import com.chronos.agenda.ui.adapter.AlunosAdapter
import com.chronos.agenda.ui.delegate.AlunoDelegate
import com.chronos.agenda.ui.service.AlunoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_lista_alunos.view.*


/**
 * Created by John Vanderson M L on 25/02/2018.
 */
class ListaAlunosAct : AppCompatActivity(),AlunoDelegate {


    private val view by lazy {
        window.decorView
    }

    private val listaAlunos by lazy{
        view.lista_alunos
    }

    private val swipe by lazy {
        view.swipe_lista_aluno
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_alunos)

        listaAlunos.setOnItemClickListener { _, _, position, _ ->
            val aluno = listaAlunos.getItemAtPosition(position) as Aluno
            val intentVaiProFormulario = Intent(this, FormularioAct::class.java)
            intentVaiProFormulario.putExtra("aluno", aluno)
            startActivity(intentVaiProFormulario)
        }


        swipe.setOnRefreshListener {
            buscarAlunos()
        }
        val novoAluno = view.novo_aluno
        novoAluno.setOnClickListener {
            startActivity(Intent(this, FormularioAct::class.java))
        }

        registerForContextMenu(listaAlunos)


    }

    override fun onResume() {
        super.onResume()
        preencherLista()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_alunos, menu);

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_enviar_notas -> EnviaAlunosTask(this).execute()
            R.id.menu_baixar_provas -> {
                startActivity(Intent(this, ProvasActivity::class.java))
            }
            R.id.menu_mapa -> {

                startActivity(Intent(this, MapaActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val aluno = listaAlunos.getItemAtPosition(info.position) as Aluno

        val itemLigar = menu?.add("Ligar")

        itemLigar?.setOnMenuItemClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 123)
            } else {
                val intentLigar = Intent(Intent.ACTION_CALL)
                intentLigar.data = Uri.parse("tel:${aluno.telefone}"  )
                startActivity(intentLigar)
            }
            false
        }

        val itemSMS = menu?.add("Enviar SMS")
        val intentSMS = Intent(Intent.ACTION_VIEW)
        intentSMS.data = Uri.parse("sms: ${aluno.telefone}")
        itemSMS?.intent = intentSMS

        val itemMapa = menu?.add("Visualizar no mapa")
        val intentMapa = Intent(Intent.ACTION_VIEW)
        intentMapa.data = Uri.parse("geo:0,0?q=${aluno.endereco}")
        itemMapa?.intent = intentMapa

        val itemSite = menu?.add("Visitar site")
        val intentSite = Intent(Intent.ACTION_VIEW)

        var site = aluno.site
        if (!site.startsWith("http://")) {
            site = "http://" + site
        }

        intentSite.data = Uri.parse(site)
        itemSite?.intent = intentSite

        val deletar = menu?.add("Deletar")

        deletar?.setOnMenuItemClickListener {

            val api  = AgendaAPI()
            api.deletarAluno(aluno.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val dao = AlunoDAO(this)
                        dao.deleta(aluno)
                        dao.close()

                    },{
                        e ->
                        Log.e("onFailure",e.message)

                    },{
                        preencherLista()
                    })


            false
        }

    }
    override fun atualizarLista() {
        preencherLista()
        swipe.isRefreshing = false
    }

    private fun buscarAlunos(){
        AlunoService(this).sicronicarDados(this)
    }

    private fun preencherLista(){
        val dao =  AlunoDAO(this);
        val alunos = dao.buscaAlunos();
        dao.close();

        val adapter =  AlunosAdapter(this, alunos);
        listaAlunos.adapter = adapter

    }
}