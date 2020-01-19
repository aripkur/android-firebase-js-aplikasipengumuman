window.onload = function() {
    cekUserLogin();
};

function cekUserLogin(){
    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
            var uid = user.uid;
            if (uid === "s8o3wvDwMWdBSnpE2Blz1zWCX2u1"){
                window.location.href = "index.html";
            }else{
                alert("Anda tidak memiliki hak akses website ini");
                firebase.auth().signOut().then(function() {
                    window.location.href = "login.html";
                });
            }
        }
    });      
}

function login(){
    $('#form').on('submit', function(e) {
        e.preventDefault();
        let userEmail = $('#inputEmail').val();
        let userPassword = $('#inputPassword').val();

        firebase.auth().signInWithEmailAndPassword(userEmail, userPassword).catch(function(error) {
            
            var errorCode = error.code;
            if(errorCode === 'auth/wrong-password'){
                alert("password salah");
                $('#inputPassword').focus();
            }
            if(errorCode === 'auth/user-not-found'){
                alert("email salah");
                $('#inputEmail').focus();
            }
          });
    });
}