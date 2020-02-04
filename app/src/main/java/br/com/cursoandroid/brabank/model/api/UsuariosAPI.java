package br.com.cursoandroid.brabank.model.api;

import java.util.List;

import br.com.cursoandroid.brabank.model.entity.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuariosAPI {

    //Todos os métodos necessitam de um CALL

    @GET("/usuarios")
    Call<List<Usuario>> listarTodos();

    @GET("/usuarios/email/{email}")
    Call<Usuario> buscarPorEmail(@Path("email") String email);

    @POST("/usuarios")
    Call<Void> inserir(@Body Usuario usuario);

}
