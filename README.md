Shoppingcart
============
I've tried to keep the code base as small as possible while implementing the requirements.
The main logic is in the Spring MVC ShoppingCartController, which when initialized can respond to HTTP requests based on this pattern:
* GET http://hostname/users
* GET http://hostname/users/<id>
* DELETE http://hostname/users/<id>
* GET http://hostname/users/<id>/shoppingcarts
* GET http://hostname/users/<id>/shoppingcarts/<id>
* POST http://hostname/users/<id>/shoppingcarts/<id>
* PUT http://hostname/users/<id>/shoppingcarts/<id>
* DELETE http://hostname/users/<id>/shoppingcarts/<id>

For POST and PUT request, application/json is accepted as content type.
A typical request body would look like this:
```javascript
{"id":1,"shoppingCarts":[{"id":0,"items":["New record","CD","Poster"]},{"id":1,"items":["Hard drive"]}]}
```
The Jackson JSON parser is used by Spring to convert to and from JSON.

Possible errors are shown in the returned HTTP status code, and can be either 404 not found if accessing a nonexistent resource or 406 not acceptable if a POST og PUT request has malformed JSON data in it.
The project is unit tested using the MockMvc part of Spring Test.

I hope you find the project satisfying :)

/Jesper