package com.chronos.agenda.tasks

import android.content.Context
import android.os.AsyncTask
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import com.chronos.agenda.converter.AlunoConverter
import com.chronos.agenda.dao.AlunoDAO
import com.chronos.agenda.web.WebClient


/**
 * Created by John Vanderson M L on 25/02/2018.
 */
class EnviaAlunosTask(context: Context) : AsyncTask<Void, Void, String>() {

    private val context = context

    private lateinit var dialog : ProgressBar

    override fun onPreExecute() {
        dialog = ProgressBar(context,null,android.R.attr.progressBarStyleLarge);
        val params = RelativeLayout.LayoutParams(100, 100);
       // context.addView(progressBar,params);
    }

    override fun doInBackground(vararg params: Void?): String {
        val dao = AlunoDAO(context)
        val alunos = dao.buscaAlunos()
        dao.close()

        val conversor = AlunoConverter()
        val json = conversor.converteParaJSON(alunos)

        val client = WebClient()
        return client.post(json)

    }

    override fun onPostExecute(resposta: String?) {
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
    }
}