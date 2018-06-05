package modelo;
//usa a pattern builder
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorSenha {
    private static final String CAIXABAIXA = "abcdefghijklmnopqrstuvwxyz";
    private static final String CAIXAALTA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITOS = "0123456789";
    private static final String ESPECIAIS = "!@#$%&*()_+-=[]|,./?><";
    private boolean usarCaixaBaixa;
    private boolean usarCaixaAlta;
    private boolean usarDigitos;
    private boolean usarEspeciais;
    
    private GeradorSenha(){
        throw new UnsupportedOperationException("O construtor não pode estar vazio.");
    }
    
    private GeradorSenha(GeradorSenhaConstrutor construtor) {
        this.usarCaixaBaixa = construtor.usarCaixaBaixa;
        this.usarCaixaAlta = construtor.usarCaixaAlta;
        this.usarDigitos = construtor.usarDigitos;
        this.usarEspeciais = construtor.usarEspeciais;
    }
    
    public static class GeradorSenhaConstrutor {
        private boolean usarCaixaBaixa;
        private boolean usarCaixaAlta;
        private boolean usarDigitos;
        private boolean usarEspeciais;
        
        public GeradorSenhaConstrutor(){
            this.usarCaixaBaixa = false;
            this.usarCaixaAlta = false;
            this.usarDigitos = false;
            this.usarEspeciais = false;
        }
        
        public GeradorSenhaConstrutor usarCaixaBaixa(boolean usarCaixaBaixa){
            this.usarCaixaBaixa = usarCaixaBaixa;
            return this;
        }
        public GeradorSenhaConstrutor usarCaixaAlta(boolean usarCaixaAlta){
            this.usarCaixaAlta = usarCaixaAlta;
            return this;
        }
        
        public GeradorSenhaConstrutor usarDigitos(boolean usarDigitos){
            this.usarDigitos = usarDigitos;
            return this;
        }
        
        public GeradorSenhaConstrutor usarEspeciais(boolean usarEspeciais){
            this.usarEspeciais = usarEspeciais;
            return this;
        }
        
        public GeradorSenha construir() {
            return new GeradorSenha(this);
        }
    }
    
    public String gerar(int comprimento){
        //validação de parâmetro
        if(comprimento<=0){
            return "";
        }
        //variáveis
        StringBuilder senha = new StringBuilder(comprimento);
        Random aleatorio = new Random(System.nanoTime());
        
        //pega as categorias que serão usadas
        List<String> categorias = new ArrayList<>(4);
        if(usarCaixaBaixa) {
            categorias.add(CAIXABAIXA);
        }
        if(usarCaixaAlta) {
            categorias.add(CAIXAALTA);
        }
        if(usarDigitos){
            categorias.add(DIGITOS);
        }
        if(usarEspeciais){
            categorias.add(ESPECIAIS);
        }
        
        //constrói a senha
        for(int i=0; i<comprimento; i++){
            String categoria = categorias.get(aleatorio.nextInt(categorias.size()));
            int posicao = aleatorio.nextInt(categoria.length());
            senha.append(categoria.charAt(posicao));
        }
        return new String(senha);
    }
}
