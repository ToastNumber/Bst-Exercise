Version of 26 February 2016.

Deadline Tuesday 1st March.

There are two parts for the exercise:

  (i) Explained in Bst.java

  (ii) Explained in Table.java

HINTS
-----

* Instead of having a field "root", you can have two fields "key" and
  "value" in the class Fork that implements Bst. This is the first
  option. An alternative option if to let the root be an entry. In my
  own sample solution, I choose the first option. There are, of
  course, many other sensible options. I leave this up to you.

* delete() becomes a bit more complicated than the one given in the
  lecture code. 

* saveInOrder() are a bit tricky at your level.

* balanced() is the trickiest method to implement. You will have to do
  some research. Here is how I do it in my sample solution. I create
  an array with the entries in the tree. Then I make the middle entry
  to be the root, and then I recursively work with the sub-array
  before the root, and the sub-array after the root. I use a private
  helper method for that. In order to work with sub-arrays, I use two
  variables "start" and "end", in recursive calls, which indicate the
  sub-array I am working with. This should give balancing in linear
  time and also linear space.

  It is tricky to create an array of generic type. I do it like this:

    Entry<Key,Value> e = new Entry<>(key,value);
    @SuppressWarnings("unchecked")
    Entry<Key,Value>[] a = (Entry<Key,Value>[]) Array.newInstance(e.getClass(), size());

  See https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html

  The first line of my recipe assumes that you have a key and a value
  that you can use. If you already have an entry, you can use it to
  get its class, instead of our e.