const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.kirimNotif = functions.database.ref('/Pengumuman/{id}').onCreate((snap, context) => {
    const snapshot = snap.val();
  
    const judul = snapshot.judul;
    const isi = snapshot.isi;
    const pengumumanid = context.params.id;
    const usergroup = snapshot.usergroup;
    const status = snapshot.status;

    const waktuterbit = snapshot.waktuterbit;
    const buatnegative = waktuterbit * -1;
    
    let message;
    let topic;
    let condition;
  
    admin.database().ref("/Pengumuman/"+pengumumanid).update({
      waktuterbit: buatnegative
    });
    
    if(status === "terbit"){
        if(usergroup === "siswa, guru"){
            condition = "'guru' in topics || 'siswa' in topics";
            message = {
                data: {
                  judul: judul,
                  isi: isi,
                  pengumumanid: pengumumanid
                },
                condition: condition
            };
            return admin.messaging().send(message);
        }
        else if (usergroup === "guru"){
            topic = 'guru';
            message = {
                data: {
                  judul: judul,
                  isi: isi,
                  pengumumanid: pengumumanid
                },
                topic: topic
            };
            return admin.messaging().send(message);
        }
        else if (usergroup === "siswa"){
            topic = 'siswa';
            message = {
                data: {
                  judul: judul,
                  isi: isi,
                  pengumumanid: pengumumanid
                },
                topic: topic
            };
            return admin.messaging().send(message);
        }
        else{
            console.log("gagal kirim");
        }
    }
});
  
exports.hitungWaktu = functions.database.ref('/DetailPengumuman/{id}').onCreate((snap, context) => {
    const datane = snap.val();
    const waktuterbit = Number(datane.waktuterbit);
    const waktusampai = Number(datane.waktusampai);
    const selisih = waktusampai - waktuterbit;
    const id = context.params.id;
    
    admin.database().ref("/DetailPengumuman/"+id).update({
        selisih: selisih
      }); 
  
  });

exports.tanggalDaftarUser = functions.database.ref('/Users/{id}').onCreate((snap, context) => {
    const snapshot = snap.val();
    const tanggaldaftar = snapshot.tanggalDaftar;
    const userid = context.params.id;
    const buatnegative = tanggaldaftar * -1;

    return admin.database().ref("/Users/"+userid).update({
        tanggalDaftar: buatnegative
      });
});

exports.cekWaktuSekarang = functions.https.onRequest((req, res) => {
  const waktuSekarang = new Date().getTime();
  admin.database().ref('/Pengumuman').once('value').then(snap =>{
    snap.forEach(data =>{      
      if( data.val().status === "pending"){
          const waktuTerbit = Math.abs(data.val().waktuterbit);
          const idPengumuman = data.val().pengumumanid;
          if(waktuSekarang >= waktuTerbit){
              admin.database().ref('/Pengumuman/'+idPengumuman).update({
                status: "terbit"
              });
          }
      }
    });
  });
  res.status(200).send("ora error");
});

exports.kirimNotifPending = functions.database.ref('/Pengumuman/{id}/status').onUpdate((snap, context) => {
  const pengumumanid = context.params.id;
  admin.database().ref('/Pengumuman/'+pengumumanid).once('value').then(snap =>{
      const judul = snap.val().judul;
      const isi = snap.val().isi;
      const usergroup = snap.val().usergroup;
    
      let message;
      if(usergroup === "siswa, guru"){
        message = {
            data: {
              judul: judul,
              isi: isi,
              pengumumanid: pengumumanid
            },
            condition: "'guru' in topics || 'siswa' in topics"
        };
        return admin.messaging().send(message);
      }
      else if (usergroup === "guru"){ 
          message = {
              data: {
                judul: judul,
                isi: isi,
                pengumumanid: pengumumanid
              },
              topic: 'guru'
          };
          return admin.messaging().send(message);
      }
      else if (usergroup === "siswa"){
          message = {
              data: {
                judul: judul,
                isi: isi,
                pengumumanid: pengumumanid
              },
              topic: 'siswa'
          };
          return admin.messaging().send(message);
      }
      else{
          console.log("gagal kirim", usergroup);
      }
  });
});