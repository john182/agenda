package com.chronos.agenda.ui.service

import android.content.Context
import android.util.Log
import com.chronos.agenda.api.AgendaAPI
import com.chronos.agenda.dao.AlunoDAO
import com.chronos.agenda.model.Aluno
import com.chronos.agenda.ui.delegate.AlunoDelegate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by John Vanderson M L on 27/02/2018.
 */
class AlunoService(val context: Context) {

    private val api  = AgendaAPI()

    fun sicronicarDados(delegate: AlunoDelegate){


        api.buscarAlunos()

        api.buscarAlunos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    val alunoDAO = AlunoDAO(context)
                    alunoDAO.sicronizarDados(it.alunos)
                    alunoDAO.close()
                    Log.i("salvo","aluno salvo com sucesso")
                },{
                    e ->
                    Log.e("onFailure",e.message)

                },{
                    delegate.atualizarLista()
                })
    }

    fun enviarAluno(aluno: Aluno){
        api.salvarAluno(aluno)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    Log.i("salvo","aluno salvo com sucesso")
                },{
                    e -> e.printStackTrace()
                },{

                })
    }

    fun deletarAluno(id:String):Boolean{
        var exluido =false
        api.deletarAluno(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    exluido =   true
                },{
                    e ->
                    Log.e("onFailure",e.message)

                },{
                   exluido =   true
                })
        return exluido
    }
}