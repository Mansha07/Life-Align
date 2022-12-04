import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JLabel;

class LifeAlign extends JFrame
{
  private int arriveTime;
  private int burstTime;
  private int priority;
  private String identity; // process number(eg. P1)
  private int turnaround; // time taken to submit and completion of our operation
  private int waiting;
  private int finishTime;  // process finish scheduling time
  private boolean status;  // to know that process been scheduling already or not
   
  public LifeAlign(String input1,int input2, int input3, int input4, boolean input5 )
  {
      identity = input1;
      setArriveTime(input2);
      setBurstTime(input3);
      setPriority(input4);
      setStatus(input5);
  }

  public LifeAlign()
  {
     JFrame frame = new JFrame("LifeAlign");
     String[] choice = {"3","4","5","6","7","8","9","10"};
     JComboBox<String> combo = new JComboBox<String>(choice);
     JPanel panel = new JPanel();
     JLabel label = new JLabel("Select amount of Tasks that you want to do");
     ActionListener ActionListener = new ActionListener() // after user choose how many process he/she want, let user insert input
     {
         public void actionPerformed(ActionEvent e)
         {
          int index=1;
          JFrame frame2 = new JFrame("Kindly, enter the required data");
          JPanel panel2 = new JPanel();
          String item = (String)combo.getSelectedItem();
          int number = Integer.parseInt(item)+1;
          JTable table = new JTable(number,4);
          JButton btn = new JButton("\"LifeAlign\"");
          table.setValueAt("Enter Tasks",0,0);
          table.setValueAt("Start Time",0,1);
          table.setValueAt("Duration",0,2);
          table.setValueAt("Set Priority",0,3);

          for(int i=1; i<number; i++)
          {
              table.setValueAt("P"+index,i,0); // set each process initial identity(process number)
              table.setValueAt(0,i,3); // set 0 for each cell of priority
              index++;
          }
      
          ActionListener btnActionListener = new ActionListener() // start LifeAlign after user click "LifeAlign" button
          {
              public void actionPerformed(ActionEvent e)
              {
                  ArrayList<LifeAlign> initialProcess = new ArrayList<LifeAlign>();
                  ArrayList<LifeAlign> initialProcess2 = new ArrayList<LifeAlign>();
                  ArrayList<LifeAlign> initialProcess3 = new ArrayList<LifeAlign>();
                  ArrayList<LifeAlign> priorityResult = new ArrayList<LifeAlign>();
                  for(int i=1; i<number; i++) //Retrieve each value in the table and put into arrayList<LifeAlign>
                  {
                     String id = (String)table.getValueAt(i,0);
                     Object l = table.getValueAt(i,1);
                     Object k = table.getValueAt(i,2);
                     Object j = table.getValueAt(i,3);
                     try{    
                     int at = Integer.parseInt(l.toString());
                     int bt = Integer.parseInt(k.toString());
                     int pt = Integer.parseInt(j.toString());
                     initialProcess.add(new LifeAlign(id,at,bt,pt,false));
                     initialProcess2.add(new LifeAlign(id,at,bt,pt,false));
                     initialProcess3.add(new LifeAlign(id,at,bt,pt,false));
                    }catch(NullPointerException ext)  //if user enter null value, remine him/her
                    {
                        JDialog dialog = new JDialog(frame2,"Error!");
                        JPanel errorPanel = new JPanel();
                        JLabel errorMessage = new JLabel("Error: Null Input,please check again");
                        errorPanel.add(errorMessage);
                        dialog.add(errorPanel);
                        dialog.setSize(350,100);
                        dialog.setVisible(true);
                        dialog.setResizable(false);
                        break;
                    }
                  }
                  priorityResult = getPriority(number-1,initialProcess); //call the function to calculate non premptive priority LifeAlign
                  JFrame frame3 = new JFrame("Result");      
                  JTable ganttChart1 = new JTable(2,priorityResult.size()+1);   // create table for show result after LifeAlign
                  int rol=0;
                  int col=0;
                  for(int i=0; i<priorityResult.size(); i++) //loop to set the values of each cells in gantt chart(priority and non SJF)
                  {
                      ganttChart1.setValueAt(priorityResult.get(i).getIdentity(),rol,col);  // set process number according arrangement
                      //set each process's start time and finish time
                      if(i==0) 
                      {
                          ganttChart1.setValueAt(priorityResult.get(0).getArriveTime(),rol+1,col); 
                        
                      }
                        else 
                        {
                            ganttChart1.setValueAt(priorityResult.get(i-1).getFinishTime(),rol+1,col); 
                            
                            if(i==priorityResult.size()-1)
                            {
                                ganttChart1.setValueAt(priorityResult.get(i).getFinishTime(),rol+1,col+1);
    
                            }
                        }
                      col++;
                  } 
                  rol=0;
                  col=0;
                  
                  HashMap<String,String> eachProcessTurn = new HashMap<String,String>(); 
                  HashMap<String,String> eachProcessWait = new HashMap<String,String>();
                  for(LifeAlign xx: priorityResult) //Store Waiting time and turnaround time of each process(non premptive priority) 
                  {
                      eachProcessTurn.put(xx.getIdentity(),String.valueOf(xx.getTurnaround()));
                      eachProcessWait.put(xx.getIdentity(),String.valueOf(xx.getWaiting()));
                  }
                  
                  HashMap<String,String> eachProcessTurn2 = new HashMap<String,String>();
                  HashMap<String,String> eachProcessWait2 = new HashMap<String,String>();
                  ArrayList<LifeAlign> check = new ArrayList<LifeAlign>();
                 
                  
                  for(LifeAlign xx: check) //Store Waiting time and turnaround time of each process(premptive SJF)
                  {
                      eachProcessTurn2.put(xx.getIdentity(),String.valueOf(xx.getTurnaround()));
                      eachProcessWait2.put(xx.getIdentity(),String.valueOf(xx.getWaiting()));
                  }
                  
                  HashMap<String,String> eachProcessTurn3 = new HashMap<String,String>();
                  HashMap<String,String> eachProcessWait3 = new HashMap<String,String>();
                  
                  
                  int priorityTotalTurn = getSumTurn(priorityResult); //get total turnaround time
                
                  float priorityAVGTurn = getAvgTurn(priorityResult,number-1); //get average turnaround time
                  
                  int priorityTotalWait = getSumWait(priorityResult); //get total waiting time
                 
                  float priorityAVGWait = getAvgWait(priorityResult, number-1);  //get average waiting time
                 
                  JPanel gridBoard = new JPanel(new GridLayout(3,0,5,5));
                  JPanel board = new JPanel(new BorderLayout());
                  JPanel board2 = new JPanel(new BorderLayout());
                  JPanel board3 = new JPanel(new BorderLayout());
                  JButton calculate = new JButton("Calculation");
                  ActionListener calListener = new ActionListener() //call the frame to show calculation for each LifeAlign algorithms
                  {
                      public void actionPerformed(ActionEvent cal)
                      {
                          JFrame frame4 = new JFrame("Calculation");
                          JPanel calGridPanel = new JPanel(new GridLayout(3,0,5,5));
                          JTextArea calresult1 = new JTextArea("1.Non Preemptive Priority LifeAlign Total Turnaround Time:" + priorityTotalTurn + "\n" +"2.Non Preemptive Priority LifeAlign Average Turnaround Time:" + priorityAVGTurn + "\n" + "3.Non Preemptive Priority LifeAlign Total Waiting Time:" + priorityTotalWait + "\n" + "4.Non Preemptive Priority LifeAlign Average Waiting Time:" + priorityAVGWait + "\n" + "5.Each Process Turnaround time:" + eachProcessTurn + "\n" + "6.Each Process Waiting Time:" + eachProcessWait + "\n");
                           calGridPanel.add(calresult1);
                         frame4.add(calGridPanel);
                          frame4.setSize(1000,1000);
                          frame4.setVisible(true);
                      }
                  };
                  calculate.addActionListener(calListener);
                  JLabel text = new JLabel("Non Preemptive Priority LifeAlign");
                  JLabel text2 = new JLabel("Premptive SJF LifeAlign");
                  JLabel text3 = new JLabel("Non Premptive SJF LifeAlign");
                  board.add(text,"North");
                  board.add(ganttChart1,"Center");
                  board2.add(text2,"North");
                 
                  board3.add(text3,"North");
                 
                  board3.add(calculate,"South");
                  gridBoard.add(board);
                  gridBoard.add(board2);
                  gridBoard.add(board3);
                  frame3.add(gridBoard);
                  frame3.setSize(1000,1000);
                  frame3.setVisible(true);
              }
          };
          btn.addActionListener(btnActionListener);
          panel2.add(table);
          panel2.add(btn);
          frame2.add(panel2);
          frame2.setSize(500,500);
          frame2.setVisible(true);
          frame2.setResizable(false);
         }
     };
     combo.addActionListener(ActionListener);
     panel.add(combo);
     panel.add(label);
     frame.add(panel);
     frame.setSize(500,500);
     frame.setVisible(true);
     frame.setResizable(false);
  }
  
  public void setArriveTime(int arrive)   //set the value of Arrive time for that process
  {
      arriveTime = arrive;
  }
    
  public int getArriveTime() //get the value of Arrive time for that process
  {
      return arriveTime;
  }
  
  public void setBurstTime(int burst)
  {
      burstTime = burst;
  }
  
  public int getBurstTime()
  {
      return burstTime;
  }
  
  public void setPriority(int pr)
  {
      priority = pr;
  }
  
  public int getPriority()
  {
      return priority;
  }
  
  public void setStatus(boolean s) //set the value of status for that process
  {
      status = s;
  }
    
  public boolean getStatus() //get the value of status for that process
  {
      return status;
  }
  
  public String getIdentity() 
  {
      return identity;
  }
  
  public void setTurnaround(int setT)
  {
      turnaround = setT;
  }
  
  public int getTurnaround()
  {
      return turnaround;
  }
  
  public void setWaiting(int setW)
  {
      waiting = setW;
  }
  
  public int getWaiting() 
  {
      return waiting; 
  }
  
    public void setFinishTime(int setF)
  {
      finishTime = setF;
  }
  
  public int getFinishTime()
  {
      return finishTime;
  }

  /**
   * Calculate total turnaround time
   */
  public int getSumTurn(ArrayList<LifeAlign> result1)  
  {
     int sumTurnaround=0;
     for(LifeAlign i: result1) 
     {
        sumTurnaround += i.getTurnaround();
     }     
     return sumTurnaround;
  }
  
  /**
   * Calculate total waiting time
   * 
   */
  public int getSumWait(ArrayList<LifeAlign> result2) 
  {
     int sumWaiting=0;
     for(LifeAlign i: result2) 
     {
        sumWaiting += i.getWaiting();
     }  
     return sumWaiting;
  }
  
  /**
   * Calculate average turnaround time
   * 
   */
  public float getAvgTurn(ArrayList<LifeAlign> result3, int num) 
  {
      float sumT = getSumTurn(result3);
      float sumW = getSumWait(result3);
      float avgTurn = sumT/num;
      return avgTurn;
  }
  
  /**
   * 
   * Calculate average waiting time
   */
  public float getAvgWait(ArrayList<LifeAlign> result4, int num) 
  {
      float sumW = getSumWait(result4);
      float avgWait = sumW/num;
      return avgWait;
  }
  
  /**
   * Get non premptive priority LifeAlign result
   * 
   * parameter:
   * totalProcess == number of process user want
   * initialProcess == input that user enter for each process
   */
  public ArrayList<LifeAlign> getPriority(int totalProcess, ArrayList<LifeAlign> initialProcess) 
  {
      ArrayList<LifeAlign> newProcess = new ArrayList<LifeAlign>();
      ArrayList<LifeAlign> checkProcess = new ArrayList<LifeAlign>();
      ArrayList<LifeAlign> oldProcess = new ArrayList<LifeAlign>();
      oldProcess = initialProcess;
      int processNum = totalProcess;
      LifeAlign temp;
      
      for(int i=0; i<processNum; i++) //to find which process is the first process
     {
         for(int j=0; j<processNum-i-1; j++)
         {
             if(oldProcess.get(j).getArriveTime()> oldProcess.get(j+1).getArriveTime()) //swap according arrive time
             {
              temp = oldProcess.get(j);
              oldProcess.set(j,oldProcess.get(j+1));
              oldProcess.set(j+1,temp);
             }
             
             else if(oldProcess.get(j).getArriveTime() == oldProcess.get(j+1).getArriveTime()) 
             {
              if(oldProcess.get(j).getPriority() > oldProcess.get(j+1).getPriority()) //swap according priority if both arrive time same
              {
              temp = oldProcess.get(j);
              oldProcess.set(j,oldProcess.get(j+1));
              oldProcess.set(j+1,temp);
              }
              
              else if(oldProcess.get(j).getPriority()==oldProcess.get(j+1).getPriority())
              {
                  if(oldProcess.get(j).getBurstTime()>oldProcess.get(j+1).getBurstTime())//swap according burst time if priority and arrive time same
                  {
                   temp = oldProcess.get(j);
                   oldProcess.set(j,oldProcess.get(j+1));
                   oldProcess.set(j+1,temp);
                }
              }
            }
        }   
     }
     newProcess.add(oldProcess.get(0));  //successfully find the first process been execute and put into newProcess for showing final result 
     int timer=0; 
     
     // set timer value as first process finish time 
     if(oldProcess.get(0).getArriveTime() != 0)  
     {
         timer = timer + oldProcess.get(0).getArriveTime() + oldProcess.get(0).getBurstTime(); 
     }
     else
     {
         timer = oldProcess.get(0).getBurstTime(); 
     }
     
     oldProcess.remove(0); //remove finished shceduling process
     newProcess.get(0).setFinishTime(timer);
     int index =1;
      for(int a=0; a<processNum-1; a++)
     {
         for(int b=0; b<oldProcess.size(); b++) //check which process arrived after first process finish executed
         {
             if(oldProcess.get(b).getArriveTime()<timer) 
             {
                 checkProcess.add(oldProcess.get(b)); //put arrived process in checkProcess for further LifeAlign
             }
             else
             {
                 continue;
             }
         }
         
         //if has not any process arrived after first process finish executed, put shortest arrive time process into checkProcess
         if(checkProcess.size()==0) 
         {
             for(int i=0; i<oldProcess.size(); i++)
             {
               checkProcess.add(oldProcess.get(0));
             }
         }
         
         //LifeAlign further process
         for(int c=0; c<checkProcess.size();c++)
         {
             for(int d=0; d<checkProcess.size()-1-c;d++)
             {
                 if(checkProcess.get(d).getPriority()>checkProcess.get(d+1).getPriority())
                 {
                     temp = checkProcess.get(d);
                     checkProcess.set(d,checkProcess.get(d+1));
                     checkProcess.set(d+1,temp);        
                 }
                 
                 else if(checkProcess.get(d).getPriority()==checkProcess.get(d+1).getPriority())
                 {
                     if(checkProcess.get(d).getBurstTime()>checkProcess.get(d+1).getBurstTime())
                     {
                         temp = checkProcess.get(d);
                         checkProcess.set(d,checkProcess.get(d+1));
                         checkProcess.set(d+1,temp);
                     }
                 }
             }
         }
         newProcess.add(checkProcess.get(0));  //put finished LifeAlign process 
         oldProcess.remove(checkProcess.get(0)); //remove process that finished LifeAlign
         timer = timer + checkProcess.get(0).getBurstTime();
         newProcess.get(index).setFinishTime(timer);
         checkProcess.clear(); 
         index++;
     }
      
     index = 0; 
     for(LifeAlign i: newProcess) //find each process turnaround time,waiting time 
     {
        i.setTurnaround((i.getFinishTime() - i.getArriveTime()));
        i.setWaiting(i.getTurnaround()-i.getBurstTime());
        index++;
     }  

     return newProcess;
  }
  
  
   /**
   * Get non premptive SJF LifeAlign result
   * 
   * parameter:
   * totalProcess == number of process user want
   * initialProcess == input that user enter for each process
   */
  public ArrayList<LifeAlign> getNonSJF(int totalProcess, ArrayList<LifeAlign>initialProcess2) 
  {
      int processNum = totalProcess;
      int process_arrive_Time;
      int process_burst_Time;
      ArrayList<LifeAlign> process = new ArrayList<LifeAlign>(); // process without LifeAlign
      ArrayList<LifeAlign> temp = new ArrayList<LifeAlign>(); // final arragement of process after LifeAlign(result)
      process = initialProcess2;
      int st=0, tot=0; // st means service time and tot means determine how many process has completed
      float totalwt=0, totalta=0;
      while(true){
            int min=99,c=processNum; // min means to find the minimum burst time and c means to find the specific process 
            if (tot==processNum) // if completed process is same as total number of process then exit loop
                break;
            
            for (int i=0;i<processNum;i++)
            {
                if ((process.get(i).getArriveTime()<=st) && (process.get(i).getStatus()==false) && (process.get(i).getBurstTime()<min))
                {   
                    min=process.get(i).getBurstTime();
                    c=i;
                }
            }
            
            if (c==processNum) // if c is not updated increase service time 
                st++;
            else
            {
                process.get(c).setFinishTime(st+process.get(c).getBurstTime());
                st=st+process.get(c).getBurstTime();
                process.get(c).setTurnaround(process.get(c).getFinishTime()-process.get(c).getArriveTime());
                process.get(c).setWaiting(process.get(c).getTurnaround()-process.get(c).getBurstTime());
                process.get(c).setStatus(true);
                tot++;  
                temp.add(process.get(c));               
            }
        }
        
        return temp;
  }
  
   /**
   * Get premptive SJF LifeAlign result
   * 
   * parameter:
   * totalProcess == number of process user want
   * initialProcess == input that user enter for each process
   */
  public ArrayList<LifeAlign> getPSJF(int totalProcess, ArrayList<LifeAlign> initialProcess3) 
  {
      int processNum;
      int process_arrive_Time;
      int process_burst_Time;
      ArrayList<LifeAlign> process = new ArrayList<LifeAlign>(); // process without LifeAlign
      ArrayList<LifeAlign> temp2 = new ArrayList<LifeAlign>(); // final arragement of process after LifeAlign(result)
      process = initialProcess3;
      processNum = totalProcess;
      int st=0, tot=0; // st means service time and tot means determine how many process has completed
      float totalwt=0, totalta=0;
      int store_c = 0;
      int burst_Time_temp[] = new int[processNum];
      int j = processNum;
      int index=0;
      boolean isrun = false; // flag that turn true when any process is successfully running
      for(LifeAlign i: process)
      {
          burst_Time_temp[index] = i.getBurstTime();
          index++;
      }
      
      while(true){
            int min=99,c=processNum; // min means to find the minimum burst time and c means to find the specific process 
            if (tot==processNum) // if completed process is same as total number of process then exit loop
                break;
            
            if(isrun == false){
                for (int i=0;i<processNum;++i)
                {
                    if ((process.get(i).getArriveTime()<=st) && (process.get(i).getStatus()==false) && (process.get(i).getBurstTime()<min))
                    {   
                        min=process.get(i).getBurstTime();
                        c=i;
                    }
                }
            }
            else{
                for (int i=0;i<processNum;++i)
                {
                    if ((process.get(i).getArriveTime()<=st) && (process.get(i).getStatus()==false) && (process.get(i).getBurstTime()<min))
                    {   
                        if(process.get(store_c).getBurstTime()==process.get(i).getBurstTime()){
                            int min_at = process.get(store_c).getArriveTime();
                            if(min_at<process.get(i).getArriveTime()){
                                min=process.get(store_c).getBurstTime();
                                c=store_c;
                            }else{
                                min=process.get(i).getBurstTime();
                                c=i;
                            }
                        }else{
                            min=process.get(i).getBurstTime();
                            c=i;
                        }
                        
                    }
                }
            }
            
            if (c==processNum) // if c is not updated increase service time 
                st++;
            else
            {
                if(c!=store_c && process.get(store_c).getBurstTime()!=0 && isrun == true)
                {
                    int k = store_c + 1;
                    process.add(new LifeAlign("P" + k,process.get(store_c).getArriveTime(),process.get(store_c).getBurstTime(),0,false));
                    process.get(j).setFinishTime(st);
                    temp2.add(process.get(j));
                    j = j + 1;
                }
                process.get(c).setBurstTime(process.get(c).getBurstTime()-1);
                st++;
                store_c = c;
                isrun = true;
                if(process.get(c).getBurstTime()==0)
                {
                    process.get(c).setFinishTime(st);
                    process.get(c).setStatus(true);
                    tot++;
                    temp2.add(process.get(c));   
                }               
            }
        }
        
        for(int i=0;i<processNum;i++)
        {
            process.get(i).setTurnaround(process.get(i).getFinishTime()-process.get(i).getArriveTime());
            process.get(i).setWaiting(process.get(i).getTurnaround()-burst_Time_temp[i]);
        
        }
         
        return temp2;
  }
  
  /**
   * This is a main for this class
   */
  public static void main(String args[])
  {
      new LifeAlign();
  }
}