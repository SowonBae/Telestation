<template>
  <div class="spyfallend border">
    <div v-if="(!spyWin && !citizenWin) && !isSpy">
      <h4 style="font-family: 'GowunDodum-Regular'">스파이가 나타났습니다!</h4>
      <h4 style="font-family: 'GowunDodum-Regular'">스파이가 시민들이 있는 장소를 추리하는 중 입니다</h4>
      <div v-if="spyPlayer" class="child-borders">
        <ov-video :stream-manager="spyPlayer"/>
      </div>
    </div>
    <div v-if="(!spyWin && !citizenWin) && isSpy" class="place_check">
      <div class="place_check_h">
        <h4 style="font-family: 'GowunDodum-Regular'">당신은 스파이 입니다!</h4>
        <h4 style="font-family: 'GowunDodum-Regular'">시민들이 있는 장소를 추리해서 승리하세요!</h4>
      </div>
      <div>              
        <button class="place1 paper-btn" @click="decide('경찰서')"><h5>경찰서</h5></button>  
        <button class="place2 paper-btn" @click="decide('자동차 정비소')"><h5>자동차 정비소</h5></button>  
        <button class="place3 paper-btn" @click="decide('학교')"><h5>학교</h5></button>  
        <button class="place4 paper-btn" @click="decide('레스토랑')"><h5>레스토랑</h5></button>  
        <button class="place5 paper-btn" @click="decide('영화 촬영소')"><h5>영화 촬영소</h5></button> 
      </div>
      <div>
        <button class="place6 paper-btn" @click="decide('대사관')"><h5>대사관</h5></button>  
        <button class="place7 paper-btn" @click="decide('병원')"><h5>병원</h5></button>  
        <button class="place8 paper-btn" @click="decide('대형마트')"><h5>대형마트</h5></button>  
        <button class="place9 paper-btn" @click="decide('공연장')"><h5>공연장</h5></button>  
        <button class="place10 paper-btn" @click="decide('해변')"><h5>해변</h5></button> 
      </div>
      <div>
        <button class="place11 paper-btn" @click="decide('우주 정거장')"><h5>우주 정거장</h5></button>  
        <button class="place12 paper-btn" @click="decide('잠수함')"><h5>잠수함</h5></button>  
        <button class="place13 paper-btn" @click="decide('놀이공원')"><h5>놀이공원</h5></button>  
        <button class="place14 paper-btn" @click="decide('공항')"><h5>공항</h5></button>  
        <button class="place15 paper-btn" @click="decide('대학 연구실')"><h5>대학 연구실</h5></button> 
      </div>
      <div>
        <button class="place16 paper-btn" @click="decide('호텔')"><h5>호텔</h5></button>  
        <button class="place17 paper-btn" @click="decide('은행')"><h5>은행</h5></button>  
        <button class="place18 paper-btn" @click="decide('카지노')"><h5>카지노</h5></button>  
        <button class="place19 paper-btn" @click="decide('회사 송년회')"><h5>회사 송년회</h5></button>  
        <button class="place20 paper-btn" @click="decide('동물원')"><h5>동물원</h5></button> 
      </div>
    </div>
    <div v-show="spyWin" class="spywinfadein">   
      <h3 class="alert alert-primary">스파이 승리!</h3>
      <div v-if="spyPlayer" class="child-borders" id="spyplayerfadein">
        <ov-video :stream-manager="spyPlayer"/>
      </div>
      <h4 id="spyplayerfadein">스파이는 {{ spyName }}님 이었습니다!</h4>
    </div>
    <div v-show="citizenWin" class="spywinfadein">   
      <h3>시민 승리!</h3>
      <div class="winbox">
        <div v-if="spyPlayer" class="child-borders"  id="spyplayerfadein">
        <ov-video :stream-manager="spyPlayer"/>
        </div>
        <div style="background-color:grey; height:301px; width: 250px" class="border">
          <label for="" style="margin-top:20px"><h4>장소</h4></label>
          <img :src="placeSrc" alt="" style="width: 200px; margin:auto">
          <h4>{{place}}</h4>  
        </div>
      </div>
      <h4 id="spyplayerfadein">스파이는 {{ spyName }}님 이었습니다!</h4>
    </div>
    <div v-if="isRoomMaker & (spyWin || citizenWin)">
      <button class="paper-btn" style="width:30%; align-self: center;" @click="backToRoom()"><span>게임 선택하기</span></button>
    </div>
    <div v-if="!isRoomMaker & (spyWin || citizenWin)">
      <button class="paper-btn" style="width:30%; align-self: center;"><span>잠시만 기다려주세요</span></button>
    </div>
    <div v-if="(spyWin || citizenWin)">
      <button class="paper-btn btn-secondary" style="width:30%; align-self: center;" @click="leaveRoom()"><span>방나가기</span></button>
    </div>    
    <div class="buttonContainer">
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import OvVideo from '../Video/OvVideo.vue';

export default {
  name: 'SpyfallEnd',

  data() {
    return {      
      spyName : JSON.parse(this.spyPlayer.stream.connection.data).clientData,
      placeSrc : ""
    }
  },

  props: {
    spyPlayer: Object,
    place: String,
    isSpy: Boolean,
    gameRes: Object,
  },

  components: {
    OvVideo,
  },

  computed: {
    ...mapState([
      "publisher",
      "session",
      "subscribers",
      "spyWin",
      "citizenWin",
      "isRoomMaker",
      "myUserName",
      "sendUserEmail"
    ])
  },

  mounted() {
    const placeImg = this.gameRes.place.split(' ').join('_')
    this.placeSrc = require("../../assets/places_image/"+placeImg+".jpg")


    if(this.session.ee._events["signal:spyWin"] == undefined) {
      this.session.on('signal:spyWin', ()=>{
        this.$store.commit("SPY_WIN")
      })
    }

    if(this.session.ee._events["signal:citizenWin"] == undefined) {
      this.session.on('signal:citizenWin', () =>{
        this.$store.commit("CITIZEN_WIN")
      })
    }     
  },
  methods: {
    leaveRoom() {
      this.$store.dispatch('leaveSession')
      this.$router.push({name:'Lobby', params: { sendUserEmail: this.sendUserEmail}})
    },

    backToRoom() {
      this.sendMessageToEveryBody('', 'initSpyfall')
    },

    sendMessageToEveryBody(data, type) {
      this.session.signal({
        data: data,
        to: [],
        type: type
      })
      .then(() => {})
      .catch(error => {
        console.error(error);
      })
		}, 

    decide(here) {
      if (this.place == here) {
        this.sendMessageToEveryBody('','spyWin')
        this.$store.commit("SPY_WIN")
      } 
      else {
        this.sendMessageToEveryBody('','citizenWin')
        this.$store.commit("CITIZEN_WIN")
      }
    }
  }
}
</script>

<style scoped>
.spyfallend {
  background-color: white;
  width: 755px;
  margin: auto;
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  animation: fadein 2s;
}
@keyframes fadein {
  from {
    opacity:0;
  }
  to {
    opacity:1;
  }
}

#spyplayerfadein  {
  animation: fadein 4s;
}

.spywinfadein {
  animation: fadein 2s;
}

.spyfallend h3 {
  font-family: 'GowunDodum-Regular';
  width: 99%;
  margin: 0;
  margin-bottom: 10px;
  padding: 7px;
}

.spyfallend h4 {
  font-family: 'GowunDodum-Regular'
}
video {
  width: 400px;  
}

.winbox {
  display: flex;
  justify-content: center;
}
.place_check {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.place_check_h {
  margin-bottom: 20px;
}
.place_check button {
  height: 120px;
  width: 150px;
  position: relative;
}
.place_check button > h5 {
  font-weight: bold;
  color: white;
  margin: 0;
  background-color: rgb(116 116 116 / 83%);
  padding: 4px;
  font-family: 'GowunDodum-Regular';
}
.place_check button > div {
  height: 120px;
  width: 150px;
  background-image: url(../../assets/places_image/x.png);
  background-size: cover;
  background-position: center;
  position: absolute;
  top: 0;
}
.place1 {
  background-image: url(../../assets/places_image/경찰서.jpg);
  background-size: cover;
  background-position: center;
}
.place2 {
  background-image: url(../../assets/places_image/자동차_정비소.jpg);
  background-size: cover;
  background-position: center;
}
.place3 {
  background-image: url(../../assets/places_image/학교.jpg);
  background-size: cover;
  background-position: center;
}
.place4 {
  background-image: url(../../assets/places_image/레스토랑.jpg);
  background-size: cover;
  background-position: center;
}
.place5 {
  background-image: url(../../assets/places_image/영화_촬영소.jpg);
  background-size: cover;
  background-position: center;
}
.place6 {
  background-image: url(../../assets/places_image/대사관.jpg);
  background-size: cover;
  background-position: center;
}
.place7 {
  background-image: url(../../assets/places_image/병원.jpg);
  background-size: cover;
  background-position: center;
}
.place8 {
  background-image: url(../../assets/places_image/대형마트.jpg);
  background-size: cover;
  background-position: center;
}
.place9 {
  background-image: url(../../assets/places_image/공연장.jpg);
  background-size: cover;
  background-position: center;
}
.place10 {
  background-image: url(../../assets/places_image/해변.jpg);
  background-size: cover;
  background-position: center;
}
.place11 {
  background-image: url(../../assets/places_image/우주_정거장.jpg);
  background-size: cover;
  background-position: center;
}
.place12 {
  background-image: url(../../assets/places_image/잠수함.jpg);
  background-size: cover;
  background-position: center;
}
.place13 {
  background-image: url(../../assets/places_image/놀이공원.jpg);
  background-size: cover;
  background-position: center;
}
.place14 {
  background-image: url(../../assets/places_image/공항.jpg);
  background-size: cover;
  background-position: center;
}
.place15 {
  background-image: url(../../assets/places_image/대학_연구실.jpg);
  background-size: cover;
  background-position: center;
}
.place16 {
  background-image: url(../../assets/places_image/호텔.jpg);
  background-size: cover;
  background-position: center;
}
.place17 {
  background-image: url(../../assets/places_image/은행.jpg);
  background-size: cover;
  background-position: center;
}
.place18 {
  background-image: url(../../assets/places_image/카지노.jpg);
  background-size: cover;
  background-position: center;
}
.place19 {
  background-image: url(../../assets/places_image/회사_송년회.jpg);
  background-size: cover;
  background-position: center;
}
.place20 {
  background-image: url(../../assets/places_image/동물원.jpg);
  background-size: cover;
  background-position: center;
}
</style>