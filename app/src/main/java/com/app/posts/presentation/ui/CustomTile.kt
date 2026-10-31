package com.app.posts.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.posts.domain.model.PostModel

@Composable
fun CustomTile(post: PostModel, modifyPost: (Int)->Unit){
    Card(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
        containerColor = Color.Blue
    )) {
        Row(modifier = Modifier.wrapContentHeight().padding(16.dp)) {
            Column(modifier = Modifier. weight(1f)) {
                Text(
                    post.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = post.body,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }

            IconButton(onClick = {
                modifyPost(post.id)
            }) {
                Icon( if(post.like) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "",
                    tint = Color.White)
            }
        }
    }
}