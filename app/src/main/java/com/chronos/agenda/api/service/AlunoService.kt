package com.chronos.agenda.api.service

import com.chronos.agenda.api.model.AlunoResult
import com.chronos.agenda.model.Aluno
import io.reactivex.Observable
import retrofit2.http.*


/**
 * Created by John Vanderson M L on 26/02/2018.
 */
interface AlunoService {

    @POST("aluno")
    fun salvar(@Body aluno:Aluno):Observable<AlunoResult>

    @GET("aluno")
    fun buscarAlunos():Observable<AlunoResult>

    @DELETE("aluno/{id}")
    fun deletar(@Path("id") id:String):Observable<Unit>
}