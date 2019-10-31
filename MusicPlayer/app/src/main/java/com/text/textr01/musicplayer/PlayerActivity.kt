package com.text.textr01.musicplayer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.GestureDetectorCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_player.*
import java.net.URL

private const val DEBUG_TAG = "Gestures"
class PlayerActivity : AppCompatActivity(),
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener{

    private lateinit var mDetector: GestureDetectorCompat

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var pause: Boolean = false



    lateinit var song: String
    lateinit var artist: String
    lateinit var url: String
    lateinit var cover_image:String
    lateinit var songPosition: String
    lateinit var arrayList: ArrayList<String>
    lateinit var arrayListName: ArrayList<String>
    lateinit var arrayListArtist: ArrayList<String>
    lateinit var arrayListImage: ArrayList<String>
    lateinit var imageView: ImageView
    lateinit var songTitle:TextView
    lateinit var songArtist:TextView
    lateinit var NewPos:String
    var songPos:Int=0
    lateinit var fade:Animation


    lateinit var rl: RelativeLayout
    override fun onResume() {

        var phone:String=intent.getStringExtra("phone")
        rl=findViewById(R.id.playerlayout)


        //database reference with user
        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")

        var themeValuenew:String="jgs"

        //check token value to show user
        val tokenListener= object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                themeValuenew=p0.child("theme").getValue().toString()
                check(themeValuenew)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        RootRef.child(phone).addValueEventListener(tokenListener)






        //Toast.makeText(this,phone,Toast.LENGTH_LONG).show()
        super.onResume()
    }

    //check theme value for dark and light
    private fun check(value: String) {
        if(value.equals("0"))
        {
            rl.setBackgroundResource(R.drawable.grad8)
        }
        if(value.equals("1"))
        {
            rl.setBackgroundResource(R.drawable.back2)
        }
    }

    var url1:String="http://hck.re/Rh8KTk"
    var url2:String="http://hck.re/ZeSJFd"
    var url3:String="http://hck.re/wxlUcX"

    var shareurl:String=""
    lateinit var shareButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)


        shareButton=findViewById(R.id.share)

        mDetector = GestureDetectorCompat(this, this)
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this)

        song= intent.getStringExtra("song")
        artist = intent.getStringExtra("artist")
        url= intent.getStringExtra("url")
        cover_image= intent.getStringExtra("cover_image")
        songPosition= intent.getStringExtra("songPosition")
        arrayList= intent.getStringArrayListExtra("arrayList")

        arrayListName= intent.getStringArrayListExtra("arrayListName")
        arrayListArtist= intent.getStringArrayListExtra("arrayListArtist")
        arrayListImage= intent.getStringArrayListExtra("arrayListImage")


        fade = AnimationUtils.loadAnimation(this, R.anim.fade)
        imageView = findViewById(R.id.coverImageView)

        if(url.equals(url1))
        {
            shareButton.visibility=View.VISIBLE
            shareurl=url1

        }
        if(url.equals(url2))
        {
            shareButton.visibility=View.VISIBLE
            shareurl=url2

        }
        if(url.equals(url3))
        {
            shareButton.visibility=View.VISIBLE
            shareurl=url3

        }

        share.setOnClickListener {
            shareSong(shareurl)
        }

        songTitle=findViewById(R.id.song_title)


        songArtist=findViewById(R.id.song_artist)

        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")

        val phone:String=intent.getStringExtra("phone")
        var tokenvalue:String




        //show tokens and checking value of token
        val tokenListener= object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot)
            {
                tokenvalue=p0.child("token").getValue().toString()
                token.setText("MyTokens : "+tokenvalue)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        RootRef.child(phone).addValueEventListener(tokenListener)


        songTitle.setText(song).toString()
        songArtist.setText(artist).toString()

        songArtist.startAnimation(fade)
 //       Toast.makeText(this,songPosition,Toast.LENGTH_SHORT).show()

        val uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown)


        //glide for show image from url

        Glide.with(this)
            .load(cover_image)
            .override(300, 200)
            .into(imageView)

        imageView.startAnimation(uptodown)


        //media player to play song when touch on card
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
        mediaPlayer.start()

        playBtn.visibility=View.INVISIBLE
        pauseBtn.visibility=View.VISIBLE

        //initial seekbar to initial state
        initializeSeekBar()
        playBtn.isEnabled = false
        pauseBtn.isEnabled = true
        mediaPlayer.setOnCompletionListener {

            playBtn.isEnabled = true
            pauseBtn.isEnabled = false

            nextsong()
            Toast.makeText(this, "Initial end", Toast.LENGTH_SHORT).show()
        }

//previous button to play previous song

        prev.setOnClickListener {



            //check song is first or not
            if(songPosition.toInt()==0)
            {
                songPos=arrayList.size-1
                NewPos=arrayList[songPos]
                songPosition=songPos.toString()
            }
            else
            {//if song not first

                songPos=songPosition.toInt()-1
                NewPos=arrayList[songPos]
                songPosition=songPos.toString()
            }
            Glide.with(this)
                .load(arrayListImage[songPos])
                .override(300, 200)
                .into(imageView)

            imageView.startAnimation(uptodown)

            songTitle.setText(arrayListName[songPos]).toString()
            songArtist.setText(arrayListArtist[songPos]).toString()

            songArtist.startAnimation(fade)
            if (pause) {

                //run when pause the song

                if(mediaPlayer.isPlaying)
                {
                    mediaPlayer.stop()
                }

                playBtn.visibility=View.INVISIBLE
                pauseBtn.visibility=View.VISIBLE

                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(NewPos)
                mediaPlayer.prepare()
                mediaPlayer.start()
                pause = false
                Toast.makeText(this, "media playing..........", Toast.LENGTH_SHORT).show()
            } else {

                if(mediaPlayer.isPlaying)
                {
                    mediaPlayer.stop()
                }

                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(NewPos)
                mediaPlayer.prepare()
                mediaPlayer.start()
                playBtn.visibility=View.INVISIBLE
                pauseBtn.visibility=View.VISIBLE
                Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()

            }
            //initial seekbar to initial state
            initializeSeekBar()
            playBtn.isEnabled = false
            pauseBtn.isEnabled = true
            mediaPlayer.setOnCompletionListener {
                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                nextsong()
                Toast.makeText(this, "prev end", Toast.LENGTH_SHORT).show()

            }


        }
        //next button to play next song
        next.setOnClickListener{

            //check song index is last or not
            if(arrayList.size ==songPos+1)
            {
                songPos=0
                NewPos=arrayList[songPos]
            }
            else
            {
                songPos=songPos+songPosition.toInt()+1
                NewPos=arrayList[songPos]
            }

            Toast.makeText(this,NewPos,Toast.LENGTH_SHORT).show()
            Toast.makeText(this,arrayListImage[songPos],Toast.LENGTH_SHORT).show()
            Glide.with(this)
                .load(arrayListImage[songPos])
                .override(300, 200)
                .into(imageView)
            imageView.startAnimation(uptodown)

            songTitle.setText(arrayListName[songPos]).toString()
            songArtist.setText(arrayListArtist[songPos]).toString()

            songArtist.startAnimation(fade)
            if (pause) {

                if(mediaPlayer.isPlaying)
                {
                    mediaPlayer.stop()
                }

                playBtn.visibility=View.INVISIBLE
                pauseBtn.visibility=View.VISIBLE

                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(NewPos)
                mediaPlayer.prepare()
                mediaPlayer.start()
                pause = false
                Toast.makeText(this, "media playing..........", Toast.LENGTH_SHORT).show()
            } else {

                if(mediaPlayer.isPlaying)
                {
                    mediaPlayer.stop()
                }

                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(NewPos)
                mediaPlayer.prepare()
                mediaPlayer.start()
                playBtn.visibility=View.INVISIBLE
                pauseBtn.visibility=View.VISIBLE
                Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()

            }
            initializeSeekBar()
            playBtn.isEnabled = false
            pauseBtn.isEnabled = true
            mediaPlayer.setOnCompletionListener {
                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                nextsong()
                Toast.makeText(this, "next end", Toast.LENGTH_SHORT).show()
            }

        }

//play buttton
        playBtn.setOnClickListener {
            if (pause) {
                //run when pause true
                playBtn.visibility=View.INVISIBLE
                pauseBtn.visibility=View.VISIBLE

                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause = false
                Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()
            } else {

                //initialize media player

                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepare()
                mediaPlayer.start()
                playBtn.visibility=View.INVISIBLE
                pauseBtn.visibility=View.VISIBLE
                Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()

            }
            //initial seekbar to initial state
            initializeSeekBar()
            playBtn.isEnabled = false
            pauseBtn.isEnabled = true
            mediaPlayer.setOnCompletionListener {
                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                nextsong()
                Toast.makeText(this, "play button end", Toast.LENGTH_SHORT).show()
            }
        }
        // Pause the media player
        //and visiblitiy of play on and pause gone
        pauseBtn.setOnClickListener {

            playBtn.visibility=View.VISIBLE
            pauseBtn.visibility=View.INVISIBLE
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                pause = true
                playBtn.isEnabled = true
                pauseBtn.isEnabled = false

                Toast.makeText(this, "media pause", Toast.LENGTH_SHORT).show()
            }
        }
        // Stop the media player
        // Seek bar change listener
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    //share song function to copy url on clipboard
    private fun shareSong(songurl: String) {

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",songurl)
        clipboard.primaryClip = clip
        Toast.makeText(this,"Song url : "+songurl+" copied to clipboard..",Toast.LENGTH_LONG).show()

    }

    //next song function same as above that is use for gestrues

    private fun nextsong() {

        if(arrayList.size ==songPos+1)
        {
            songPos=0
            NewPos=arrayList[songPos]
        }
        else
        {
            songPos=songPos+songPosition.toInt()+1
            NewPos=arrayList[songPos]
        }

        Glide.with(this)
            .load(arrayListImage[songPos])
            .override(300, 200)
            .into(imageView)

        val uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown)
        imageView.startAnimation(uptodown)

        songTitle.setText(arrayListName[songPos]).toString()
        songArtist.setText(arrayListArtist[songPos]).toString()

        songArtist.startAnimation(fade)
        if (pause) {

            if(mediaPlayer.isPlaying)
            {
                mediaPlayer.stop()
            }

            playBtn.visibility=View.INVISIBLE
            pauseBtn.visibility=View.VISIBLE

            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(NewPos)
            mediaPlayer.prepare()
            mediaPlayer.start()
            pause = false
            Toast.makeText(this, "media playing..........", Toast.LENGTH_SHORT).show()
        } else {

            if(mediaPlayer.isPlaying)
            {
                mediaPlayer.stop()
            }

            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(NewPos)
            mediaPlayer.prepare()
            mediaPlayer.start()
            playBtn.visibility=View.INVISIBLE
            pauseBtn.visibility=View.VISIBLE
            Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()

        }
        initializeSeekBar()
        playBtn.isEnabled = false
        pauseBtn.isEnabled = true
        mediaPlayer.setOnCompletionListener {
            playBtn.isEnabled = true
            pauseBtn.isEnabled = false
            nextsong()
            Toast.makeText(this, "next end", Toast.LENGTH_SHORT).show()

        }

    }
//initialize for getsures
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    //gesture function
    override fun onDown(event: MotionEvent): Boolean {

        Log.d(DEBUG_TAG, "onDown: $event")
        //Toast.makeText(this,"onDown",Toast.LENGTH_LONG).show()
        return true
    }


    //gesture function to check left or right
    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        var diffY:Float=event2.getY() - event1.getY()
        var diffX:Float=event2.getX() - event1.getX()

        Log.d(DEBUG_TAG, "onFling: $event1 $event2")
        //Toast.makeText(this,event1.toString(),Toast.LENGTH_LONG).show()

        //run when left right
        if(Math.abs(diffX) > Math.abs(diffY))
        {
            //run when swipe right
            if(Math.abs(diffX) > 100 && Math.abs(velocityX)>100)
            {
                if(diffX >0)
                {
                    if(songPosition.toInt()==0)
                    {
                        songPos=arrayList.size-1
                        NewPos=arrayList[songPos]
                        songPosition=songPos.toString()
                    }
                    else
                    {

                        songPos=songPosition.toInt()-1
                        NewPos=arrayList[songPos]
                        songPosition=songPos.toString()
                    }
                    Glide.with(this)
                        .load(arrayListImage[songPos])
                        .override(300, 200)
                        .into(imageView)

                    val uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown)

                    imageView.startAnimation(uptodown)

                    songTitle.setText(arrayListName[songPos]).toString()
                    songArtist.setText(arrayListArtist[songPos]).toString()

                    songArtist.startAnimation(fade)
                    if (pause) {

                        if(mediaPlayer.isPlaying)
                        {
                            mediaPlayer.stop()
                        }

                        playBtn.visibility=View.INVISIBLE
                        pauseBtn.visibility=View.VISIBLE

                        mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(NewPos)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        pause = false
                        Toast.makeText(this, "media playing..........", Toast.LENGTH_SHORT).show()
                    } else {

                        if(mediaPlayer.isPlaying)
                        {
                            mediaPlayer.stop()
                        }

                        mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(NewPos)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        playBtn.visibility=View.INVISIBLE
                        pauseBtn.visibility=View.VISIBLE
                        Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()

                    }
                    initializeSeekBar()
                    playBtn.isEnabled = false
                    pauseBtn.isEnabled = true
                    mediaPlayer.setOnCompletionListener {
                        playBtn.isEnabled = true
                        pauseBtn.isEnabled = false
                        nextsong()
                        Toast.makeText(this, "gesture prev end", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {//run when swipe left
                    if(arrayList.size ==songPos+1)
                    {
                        songPos=0
                        NewPos=arrayList[songPos]
                    }
                    else
                    {
                        songPos=songPos+songPosition.toInt()+1
                        NewPos=arrayList[songPos]
                    }

                    Toast.makeText(this,NewPos,Toast.LENGTH_SHORT).show()
                    Toast.makeText(this,arrayListImage[songPos],Toast.LENGTH_SHORT).show()
                    Glide.with(this)
                        .load(arrayListImage[songPos])
                        .override(300, 200)
                        .into(imageView)

                    val uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown)
                    imageView.startAnimation(uptodown)

                    songTitle.setText(arrayListName[songPos]).toString()
                    songArtist.setText(arrayListArtist[songPos]).toString()

                    songArtist.startAnimation(fade)
                    if (pause) {

                        if(mediaPlayer.isPlaying)
                        {
                            mediaPlayer.stop()
                        }

                        playBtn.visibility=View.INVISIBLE
                        pauseBtn.visibility=View.VISIBLE

                        mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(NewPos)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        pause = false
                        Toast.makeText(this, "media playing..........", Toast.LENGTH_SHORT).show()
                    } else {

                        if(mediaPlayer.isPlaying)
                        {
                            mediaPlayer.stop()
                        }

                        mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(NewPos)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        playBtn.visibility=View.INVISIBLE
                        pauseBtn.visibility=View.VISIBLE
                        Toast.makeText(this, "media playing", Toast.LENGTH_SHORT).show()

                    }
                    initializeSeekBar()
                    playBtn.isEnabled = false
                    pauseBtn.isEnabled = true
                    mediaPlayer.setOnCompletionListener {
                        playBtn.isEnabled = true
                        pauseBtn.isEnabled = false
                        nextsong()
                        Toast.makeText(this, "gesture next end", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
        else
        {
           // Toast.makeText(this,"left",Toast.LENGTH_LONG).show()
        }

        return true
    }

    //gesture function to pause on long press
    override fun onLongPress(event: MotionEvent) {
        Log.d(DEBUG_TAG, "onLongPress: $event")
        Toast.makeText(this,"onLongPress",Toast.LENGTH_LONG).show()
        playBtn.visibility=View.VISIBLE
        pauseBtn.visibility=View.INVISIBLE
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            pause = true
            playBtn.isEnabled = true
            pauseBtn.isEnabled = false

            Toast.makeText(this, "media pause", Toast.LENGTH_SHORT).show()
        }
    }

    //gesture function
    override fun onScroll(
        event1: MotionEvent,
        event2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        //Toast.makeText(this,"onScroll",Toast.LENGTH_LONG).show()
        Log.d(DEBUG_TAG, "onScroll: $event1 $event2")
        return true
    }

    //gesture function
    override fun onShowPress(event: MotionEvent) {
       // Toast.makeText(this,"onShowPress",Toast.LENGTH_LONG).show()
        Log.d(DEBUG_TAG, "onShowPress: $event")
    }

    //gesture function
    override fun onSingleTapUp(event: MotionEvent): Boolean {
        //Toast.makeText(this,"onSingleTapUp",Toast.LENGTH_LONG).show()
        Log.d(DEBUG_TAG, "onSingleTapUp: $event")
        return true
    }

    //gesture function
    override fun onDoubleTap(event: MotionEvent): Boolean {
        //Toast.makeText(this,"onDoubleTap",Toast.LENGTH_LONG).show()
        Log.d(DEBUG_TAG, "onDoubleTap: $event")
        return true
    }

    //gesture function
    override fun onDoubleTapEvent(event: MotionEvent): Boolean
    {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: $event")
        //Toast.makeText(this,"onDoubleTapEvent",Toast.LENGTH_LONG).show()
        return true
    }

    //gesture function
    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        //Toast.makeText(this,"onSingleTapConfirmed",Toast.LENGTH_LONG).show()
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: $event")
        return true
    }



    // Method to initialize seek bar and audio stats
    private fun initializeSeekBar() {

        lateinit var out:String
        lateinit var out2:String
        seek_bar.max = mediaPlayer.seconds

        runnable = Runnable {
            seek_bar.progress = mediaPlayer.currentSeconds

            //give in minutes format (convert second to minute)
            out=String.format("%02d:%02d", seek_bar.progress / 60, seek_bar.progress % 60)



            tv_pass.text = "${out}"
            val diff: Int = mediaPlayer.seconds - mediaPlayer.currentSeconds
            out2=String.format("%02d:%02d",diff/60,diff%60)
            tv_due.text = "$out2"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    override fun onBackPressed() {

//on back press media stop and get back to the music list page
        if(mediaPlayer.isPlaying)
        {
            mediaPlayer.stop()
        }

        super.onBackPressed()


    }

    // Creating an extension property to get the media player time duration in seconds
    val MediaPlayer.seconds: Int
        get() {
            return this.duration / 1000
        }
    // Creating an extension property to get media player current position in seconds
    val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / 1000
        }



}
