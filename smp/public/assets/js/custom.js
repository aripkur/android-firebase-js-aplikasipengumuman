let rootRefPengumuman =  firebase.database().ref("Pengumuman");
let rootRefUser =  firebase.database().ref("Users");
let rootRefDetailPengumuman =  firebase.database().ref("DetailPengumuman");
let rootRefSettingApp =  firebase.database().ref("SettingApp");


$(document).ready(function() {
    rootRefPengumuman.orderByChild("waktuterbit").limitToFirst(5).on("value", tampilDataPengumuman);
    rootRefPengumuman.on("value", totalDataPengumuman);
    rootRefUser.on("value", totalDataUser);

    rootRefPengumuman.orderByChild("waktuterbit").on("value", tampilPengumuman);

    rootRefUser.orderByChild("tanggalDaftar").on("child_added", tampilDataUser);
});

// TOTAL DATA USER MENU DASHBOARD
function totalDataUser(data){
    let i=0;
    data.forEach(item => {
        i++;
    });
    $('#total-user').text(i);
}

// TOTAL DATA PENGUMUMAN MENU DASHBOARD
function totalDataPengumuman(data){
    let i=0;
    data.forEach(item =>{
        i++;
    });
    console.log("afdas");
    $('#total-pengumuman').text(i);
}

// TAMPIL LIST PENGUMUMAN TERBARU MENU DASHBOARD
function tampilDataPengumuman(data){
    $('#loading').addClass("d-none");
    let content = '';
    data.forEach(item =>{
        if(item.val().status == "terbit"){
            let judul = item.val().judul;
            let panjangjudul = judul.length;
            if(panjangjudul > 200 ){
                judul = judul.substr(0,200)+' ...';
            }
            content += "<div class='row col-12 pengumuman'><p class='my-auto'>" + judul +"</p></div>";
        }
    });
    $('#konten-list-pengumuman-terbaru').html(content);
}

// TAMPIL LIST PENGUMUMAN MENU PENGUMUMAN
function tampilPengumuman(data){
    $('#loading2').addClass("d-none");
    let contentTerbit = '';
    let contentPending = '';
    data.forEach(item =>{
        if(item.val().status == "terbit"){
            let judul = item.val().judul;
            let idpengumuman = item.val().pengumumanid;
            let panjangjudul = judul.length;
            if(panjangjudul > 200 ){
                judul = judul.substr(0,200)+' ...';
            }
            contentTerbit += "<div class='row col-12 pengumuman'><a href='#' onclick=detailPengumuman('"+idpengumuman+"'); class='my-auto'>" + judul +"</a></div>";
        }else if(item.val().status == "pending"){
            let judul = item.val().judul;
            let idpengumuman = item.val().pengumumanid;
            let panjangjudul = judul.length;
            if(panjangjudul > 200 ){
                judul = judul.substr(0,200)+' ...';
            }
            contentPending += "<div class='row col-12 pengumuman'><a href='#' onclick=detailPengumuman('"+idpengumuman+"'); class='my-auto'>" + judul +"</a></div>";
        }
    });
    $('#konten-list-pengumuman-terbit').html(contentTerbit);
    $('#konten-list-pengumuman-pending').html(contentPending);
}

// BUKA DETAIL PENGUMUMAN
function detailPengumuman(id){
    $('#menu-pengumuman').addClass("d-none");
    $('#menu-detail-pengumuman').removeClass("d-none");
    $("#konten-tabel tr").remove();
    console.log("id", id);
    rootRefPengumuman.child(id).once('value').then(function(snapshot) {
        let html1 = "<p class='text-justify'> <b>Judul : </b>"+snapshot.val().judul+"<br/><b> Isi : </b>"+snapshot.val().isi+"<br/><b>Penerima : </b>"+snapshot.val().usergroup+"<br/><b>Waktu Terbit : </b>"+konvert(snapshot.val().waktuterbit)+"</p>";
        $('#isi-detail-pengumuman').html(html1);
    });
    rootRefDetailPengumuman.orderByChild("idPengumuman").equalTo(id).on('child_added', function(data) {
        console.log("data", data.val().selisih);
        let iduser = data.val().userid;
        let waktuterbit = data.val().waktuterbit;
        let waktusampai = data.val().waktusampai;
        let selisih = data.val().selisih;
        rootRefUser.child(iduser).on('value', function (snapshot) {
            nama = snapshot.val().nama;
            $("#konten-tabel").append("<tr><td>"+ nama +"</td><td>"+ waktuterbit +" ms</td><td>"+ waktusampai +" ms</td><td>"+ selisih +" ms</td></tr>");
        });
      });
}

// CONVERT WAKTU
function konvert(waktufirebase) {
    let milisecond = Math.abs(waktufirebase);        
    return new Date(milisecond);
}

// INPUT PENGUMUMAN BARU MENU PENGUMUMAN
function inputPengumuman(){
    let judul = $('#judul').val();
    let isi = $('#isi').val();
    let siswa = $('#siswa:checked').val();
    let guru = $('#guru:checked').val();
    let waktukirim = $('#waktukirim').val();

    if(waktukirim == "terbit"){
        if(siswa == "ya" && guru == "ya"){
            let usergroup= "siswa, guru";
            inputDatabaseTerbit(judul, isi, usergroup);
        }else if (siswa == "ya") {
            let usergroup="siswa";
            inputDatabaseTerbit(judul, isi, usergroup);
        }else if (guru == "ya") {
            usergroup="guru";
            inputDatabaseTerbit(judul, isi, usergroup);
        }else{
            alert("silahkan pilih penerima pengumuman");
        }
    }else if(waktukirim == "pending"){
        let waktuKirim = $('#datepicker').val();
        let konvertWaktu = new Date(waktuKirim).getTime();

        if(siswa == "ya" && guru == "ya"){
            let usergroup= "siswa, guru";
            inputDatabasePending(judul, isi, usergroup, konvertWaktu);
        }else if (siswa == "ya") {
            let usergroup="siswa";
            inputDatabasePending(judul, isi, usergroup, konvertWaktu);
        }else if (guru == "ya") {
            usergroup="guru";
            inputDatabasePending(judul, isi, usergroup, konvertWaktu);
        }else{
            alert("silahkan pilih penerima pengumuman");
        }
    }
}

function inputDatabasePending(judul, isi, usergroup, konvertWaktu) {
    let pengumumanid = rootRefPengumuman.push().key;
     rootRefPengumuman.update({
            [pengumumanid]:{
                pengumumanid: pengumumanid,
                judul: judul,
                isi: isi,
                status:"pending",
                usergroup : usergroup,
                waktuterbit: konvertWaktu
            }
    });
}

function inputDatabaseTerbit(judul, isi, usergroup) {
    let pengumumanid = rootRefPengumuman.push().key;
     rootRefPengumuman.update({
            [pengumumanid]:{
                pengumumanid: pengumumanid,
                judul: judul,
                isi: isi,
                status: "terbit",
                usergroup : usergroup,
                waktuterbit: firebase.database.ServerValue.TIMESTAMP
            }
    });
}

// TAMPIL SEMUA DATA USER MENU USER
function tampilDataUser(snap){
    $('#loading3').addClass("d-none");
    let uid = snap.child("id").val();
    let nama = snap.child("nama").val();
    let email = snap.child("email").val();
    let usergroup = snap.child("usergroup").val();

    $("#tablebody").append("<tr><td>"+ nama +"</td><td>"+ email +"</td><td>"+ usergroup +"</td><td><button type='button' id="+uid+" class='btn btn-warning btn-sm' onClick='editUser(this.id)'>Edit</button> <button type='button' id="+uid+" class='btn btn-danger btn-sm' onClick='hapusUser(this.id)'>Hapus</button></td></tr>");
}

// CARI USER MENU USER
function cariUser(){
    let cari = $('#cari').val();
    let kategoriCari = $('#cariBerdasarkan').val();
    if(kategoriCari == "usergroup"){
        cari = $('#cari').val().toLowerCase();
    }
    if(kategoriCari ==""){
        alert("Silahkan lengkapi data pencarian terlebih dahulu ...");
        $('#cariBerdasarkan').focus();
    }else if(cari == ""){
        alert("Silahkan lengkapi data pencarian terlebih dahulu");
        $('#cari').focus();
    }else{
        let isitabel = '';
        $("#tablebody").empty();
        rootRefUser.orderByChild(kategoriCari).equalTo(cari).on("value", snap => {
            snap.forEach( data => {
                isitabel += "<tr><td>"+ data.val().nama +"</td><td>"+ data.val().email +"</td><td>"+ data.val().usergroup +"</td><td><button type='button' id="+data.val().id+" class='btn btn-warning btn-sm' onClick='editUser(this.id)'>Edit</button> <button type='button' id="+data.val().id+" class='btn btn-danger btn-sm' onClick='hapusUser(this.id)'>Hapus</button></td></tr>";
                
            });
            $("#tablebody").append(isitabel);
            $('#cari').val("");
            $('#cariBerdasarkan').val("");
        });
    }
}

// EDIT USER MENU USER
function editUser(id){
    $('#modalEdit').modal('show');
    rootRefUser.child(id).on("value", snap =>{
        $('[name="nama"]').val(snap.child("nama").val());
        $('[name="email"]').val(snap.child("email").val());
        $('[name="usergroup"]').val(snap.child("usergroup").val());
    })
    $( "#simpan" ).click(function() {
        let nama = $('#nama').val();
        let email = $('#email').val();
        let usergroup = $('#usergroup').val();

        rootRefUser.child(id).update({
            "nama" : nama,
            "email" : email,
            "usergroup" : usergroup
        });
        $('#modalEdit').modal('hide');
        location.reload();
    });
}

// HAPUS USER MENU USER
function hapusUser(id){
    if(confirm('Yakin ingin menghapus user ini ?')){
        rootRefUser.child(id).remove();
        location.reload();
    }
}

// USER LOGOUT
function logOut(){
    firebase.auth().signOut().then(function() {
        window.location.href = "login.html";
      });
}

window.onload = function() {
    cekUserLogin();
    $('#waktukirim').val("terbit");
};

// CEK USER LOGIN
function cekUserLogin(){
    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
            var uid = user.uid;
            if (uid !== "s8o3wvDwMWdBSnpE2Blz1zWCX2u1"){
                alert("Anda tidak memiliki hak akses website ini");
                firebase.auth().signOut().then(function() {
                    window.location.href = "login.html";
                });
            }
        } else {
            window.location.href = "login.html";
        }
    });      
}

// INPUTAN DATEPICKER
$("select#waktukirim").change(function(){
    var waktukirim = $(this).children("option:selected").val();
    if( waktukirim == "pending"){
        $("#datepicker").removeClass('d-none');
        $("#datepicker").val("");
    }else if (waktukirim == "terbit") {
        $("#datepicker").addClass('d-none'); 
    }
});
var sekarang = new Date();
$("#datepicker").datepicker({
    todayHighlight: true,
    orientation: "bottom left",
    autoclose: true,
    startDate: sekarang  
});

// GANTI PASSWORD APLIKASI ANDROID
function gantiPasswordApk(){
    let passwordApk = $('#passApk').val();
    rootRefSettingApp.update({
        passwordAplikasi: passwordApk 
    }, function(error) {
        if (error) {
          alert("Gagal ganti password aplikasi android");
        } else {
          alert("Berhasil ganti password aplikasi android");
        }
    });
}