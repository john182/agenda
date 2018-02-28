package com.chronos.agenda.converter

import com.chronos.agenda.model.Aluno
import org.json.JSONException
import org.json.JSONStringer


/**
 * Created by John Vanderson M L on 25/02/2018.
 */
class AlunoConverter {

    fun converteParaJSON(alunos: List<Aluno>): String {
        val js = JSONStringer()

        try {
            js.`object`().key("list").array().`object`().key("aluno").array()
            for (aluno in alunos) {
                js.`object`()
                js.key("nome").value(aluno.nome)
                js.key("nota").value(aluno.nota)
                js.endObject()
            }
            js.endArray().endObject().endArray().endObject()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return js.toString()
    }


}