
class Warrior {
  private String name;
  private int speed;
  private int strength;
  private int hp;
  Warrior next;

  public Warrior(String name, int speed, int str, int hp) {
    this.name = name;
    this.speed = speed;
    this.strength = str;
    this.hp = hp;
  }

  public String getName() { return this.name; }
  public int getSpeed() { return this.speed; }
  public int getStrength() { return this.strength; }
  public int getHp() { return this.hp; }

  public String toString() { return this.name + "(" +
   this.speed + ")"; }
}


interface LinkedList {
void insert(Warrior warrior);
String toString();
}

//My Class to Implement
class SortedDoublyLinkedList implements LinkedList extends Warrior{

  class Node {
  //  Node next; // the next element
  //  Node prev;
  private Node head, last;
    Node temp = new Node(Warrior.speed);
    if (head == null) {
        head = temp;
        last = head;
    } else {
        temp.next = head;
        head.prev = temp;
        head = temp;
    }
    size++;
}

    // name.setWarrior (function)
    // .prev (is for 5 to 4)

  public void insert(Warrior warrior) {
    Node temp = new Node(data);
    if (head == null) {
        head = temp;
        last = head;
    } else {
        temp.next = head;
        head.prev = temp;
        head = temp;
    }
    size++;

    /*
    Warrior current = this; //What is Warrior here for
    while (current.next != null) {
      current = current.next;
    }
    current.next = new Warrior(warrior.speed);
    */
}
  public String toString() {
    println("hi");
  }
}



class LinkedListDriver {
  public static void main(String[] args) {
    LinkedList list = new SortedDoublyLinkedList();

    System.out.println(list);
    Warrior krogg = new Warrior("Krogg", 30, 50, 200);
    list.insert(krogg);

    System.out.println(list);
    Warrior gurkh = new Warrior("Gurkh", 40, 45, 180);
    list.insert(gurkh);

    System.out.println(list);
    Warrior brynn = new Warrior("Brynn", 45, 40, 190);
    list.insert(brynn);

    System.out.println(list);
    Warrior dolf = new Warrior("Dolf", 20, 65, 210);
    list.insert(dolf);

    System.out.println(list);
    Warrior zuni = new Warrior("Zuni", 50, 35, 170);
    list.insert(zuni);

    System.out.println(list);
  }
}
