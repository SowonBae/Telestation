<template>
  <div class="lobby">
    <div class="myface">
			<div class="bouncing-text">
				<v-row class="texts">
					<div class="S1"><h1>S</h1></div>
					<div class="S2"><h1>S</h1></div>
					<div class="A1"><h1>A</h1></div>
					<div class="N"><h1>N</h1></div>
					<div class="O"><h1>O</h1></div>
					<div class="L"><h1>L</h1></div>
					<div class="J"><h1>J</h1></div>
					<div class="A2"><h1>A</h1></div>
				</v-row>
				<v-row  class="shadows">
					<div class="shadow"></div>
					<div class="shadow-two"></div>
					<div class="shadow-three"></div>
					<div class="shadow-four"></div>
				</v-row>
			</div>    
			<div id="join" v-if="!session" class="border" style="padding-bottom: 10px; background-color: white; ">
				<div id="join-dialog" class="vertical-center">
					<h1 style="margin-top:10px">LOBBY</h1>
					<div class="lobby_form">
						<div>
							<p><label>닉네임</label></p>
							<input v-model="userData.userNickname" class="paper-input" style="width:200px" type="text" required placeholder="닉네임을 입력하세요">
							<p><label>방 참여코드</label></p>
							<input v-model="joinCode" class="paper-input" style="width:200px" type="text" required placeholder="참여코드를 입력하세요">
						</div>
						<div class="joinbutton">
							<p class="text-center"><button class="paper-btn btn-lg btn-success" @click="makeRoom()">방 만들기</button></p>
							<p class="text-center"><button class="paper-btn btn-lg btn-success" @click="checkRoom(joinCode)">입장하기</button></p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<preview ref="preview" :joinCode="joinCode" :myUserName="userData.userNickname"></preview>
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import Preview from "@/components/Preview.vue";

export default {
  name: "Lobby",
	data () {
		return {
			userData : {
				userName : null,
				userEmail : null,
				userNickname : null,
			},
			joinCode: '',
			myUserName: '',
			sendUserEmail: '',
		}
	},
	components: {
		Preview,
	},
	computed: {
		...mapState([
			"session",
			"publisher",
			"subscribers",
			"mainStreamManager",
		])
	},    

	mounted() {
		localStorage.clear()
		this.getUserData()
		localStorage.clear()
	},
	
	methods: {
		checkRoom(joinCode) {
			localStorage.clear()
			localStorage.setItem("isRoomMaker", false)
			this.$store.dispatch('setRoomMaker', false)
			axios({
				method: 'post',
				url: '/api/users/update',
				data: {
					userEmail: this.userData.userEmail,
					updatedUserNickname: this.userData.userNickname
				}
			})
			.then(() =>{
				axios({
					method: 'get',
					url:`/api/rooms/${joinCode}`,
				})
				.then(()=>{
					this.$refs.preview.dialog = true							
				})
				.catch(() => {
					alert('일치하는 방이 존재하지 않습니다.')
				})
			})
		},

		makeRoom() {
			localStorage.clear()
			localStorage.setItem("isRoomMaker", true)
			this.$store.dispatch("setRoomMaker", true)
			axios({
				method: 'post',
				url: '/api/users/update',
				data: {
					userEmail: this.userData.userEmail,
					updatedUserNickname: this.userData.userNickname
				}
			})
			.then(() => {
				axios({
        method: 'post',
        url:'/api/rooms',
      })
        .then(res=>{
					this.joinCode = res.data
					this.$refs.preview.dialog = true
        })
        .catch(err=> {
          console.log(err)
        })
			})
			.catch(()=>{
				alert('이미 존재하는 닉네임입니다. 다른 닉네임을 입력해주세요')
			})
    },
		leaveSession() {
			this.$store.dispatch('leaveSession')
		},
		updateMainVideoStreamManager(data) {
			this.$store.dispatch('updateMainVideoStreamManager',data)
		},
		getUserData() {
			this.sendUserEmail = this.$route.params.sendUserEmail
			this.$store.commit("SET_SENDUSEREMAIL", this.sendUserEmail)
			axios({
				method:'POST',
				url: '/api/login/sendUser',
				data: this.sendUserEmail
			})
			.then(res => {
				this.userData.userName = res.data.userNickname
				this.userData.userNickname = res.data.userNickname
				this.userData.userEmail = res.data.userEmail
			})
			.catch(err => console.log(err))
		}
	}	
}
</script>

<style scoped>
.lobby {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: hidden;
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

.lobby div input {
  color: black;
}
.lobby .myface {
  width: 600px;
}
.go-or-make {
  display: flex;
  justify-content: center;
  align-items: center;
}
.go-or-make button {
  margin: 10px;
  width: 100px;
}
.lobby > img {
  width: 300px;
  margin-top: 100px;
  margin-bottom: 100px;
}
.vertical-center {
	text-align: center;
}
.vertical-center input{
	margin: auto;
	margin-bottom: 20px;
}
.bouncing-text {
  position: relative;
  display: flex;  
	flex-direction: column;
	justify-content:center;
  font-family: "Patrick Hand SC", sans-serif;
  margin-bottom: 120px;
  margin-top: 120px;
}
.texts {
	justify-content: center;
}
.shadows {
	margin-left: 69px;
	margin-top: 18px;
}
.S1, .S2, .A1, .N, .O, .A2 {
  color: rgb(0, 0, 0);
  margin-right: 20px;
  text-shadow: 0 3px #a1a1a1, 0 5px #a1a1a1, 0 7px #a1a1a1;
}
.L {
  color: rgb(0, 0, 0);
  margin-right: 22px;
  text-shadow: 0 3px #a1a1a1, 0 5px #a1a1a1, 0 7px #a1a1a1;
}
.J {
  color: rgb(0, 0, 0);
  margin-right: 22px;
  text-shadow: 0 3px #a1a1a1, 0 5px #a1a1a1, 0 7px #a1a1a1;
}
.S1 h1, .S2 h1, .A1 h1, .N h1, .O h1, .L h1, .J h1, .A2 h1 {
  margin: 0;
  font-size: 80px;
}
@keyframes bounce {
	0%   { transform: scale(1,1) translateY(0); }
	10%  { transform: scale(1.1,.9) translateY(0); }
	30%  { transform: scale(.9,1.1)   translateY(-55px);}
	50%  { transform: scale(1.05,.95) translateY(0); }
	58%  { transform: scale(1,1) translateY(-7px); }
	65%  { transform: scale(1,1) translateY(0);}
	100% { transform: scale(1,1) translateY(0);}
}
.S1 {
  animation: bounce 2s ease infinite;
}
.S2 {
  animation: bounce 2s ease infinite .2s;
}
.A1 {
  animation: bounce 2s ease infinite .4s;
}
.N {
  animation: bounce 2s ease infinite .6s;
}
.O {
  animation: bounce 2s ease infinite .8s;
}
.L {
  animation: bounce 2s ease infinite 1.0s;
}
.J {
  animation: bounce 2s ease infinite 1.2s;
}
.A2 {
  animation: bounce 2s ease infinite 1.4s;
}
.shadow, .shadow-two, .shadow-three, .shadow-four {
  position: absolute;
  bottom: 0;
}
.shadow:before, .shadow:after, .shadow-two:before, .shadow-two:after, .shadow-three:before, .shadow-three:after, .shadow-four:before, .shadow-four:after {
  content:"";
  position: absolute;
  background-color: rgba(0,0,0,0.4);
  width: 37px;
  height:5px;
  border-radius:50%;
  top: 0px;  
}
.bouncing-text:before {
  bottom: 0;
  animation: scale 2s linear infinite;
}
@keyframes scale {
  0% {transform: scaleX(1);}
  25% {transform: scaleX(0.4);}
  50% {transform: scaleX(1);}
  75% {transform: scaleX(0.9);}
  100% {transform: scaleX(1);}
}
.shadow:before {
  left: 0;
  animation: scale 2s linear infinite .4s;
}
.shadow:after {
  left:55px;
  animation: scale 2s linear infinite .6s;
}
.shadow-two:before {
  left:114px;
  animation: scale 2s linear infinite .8s;
}
.shadow-two:after {
  left: 176px;
  animation: scale 2s linear infinite 1.0s;
}
.shadow-three:before {
  left: 239px;
  animation: scale 2s linear infinite 1.2s;
}
.shadow-three:after {
  left: 295px;
  animation: scale 2s linear infinite 1.4s;
}
.shadow-four:before {
  left:349px;
  animation: scale 2s linear infinite 1.2s;
}
.shadow-four:after {
  left: 404px;
  animation: scale 2s linear infinite 1.4s;
}
.lobby_form {
	display: flex;
	justify-content: space-around;
}
.joinbutton {
	display: flex;
	flex-direction: column;
}
.joinbutton p {
	margin-top: 40px;
}
</style>